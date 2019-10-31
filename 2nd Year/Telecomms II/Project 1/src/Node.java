import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;


public abstract class Node {
    static final int PACKET_SIZE = 1024;

    public final byte TYPE_DATA = 0;
    public final byte TYPE_ACK = 1;
    public final byte TYPE_CONNECTION = 2;
    public final byte TYPE_CONNECTION_ACK = 3;
    public final int TYPE_POS = 0;

    public final byte FRAME_1 = 0;
    public final byte FRAME_2 = 1;
    public final byte FRAME_3 = 2;
    public final byte FRAME_4 = 3;
    public final int FRAME_POS = 1;

    public final byte HEADER_PACKET_COUNT = 1;
    public final int PACKET_NUMBER_POSITION = 2;

    public final byte WORKER_TYPE = 0;
    public final byte C_AND_C_TYPE = 1;
    public final byte BROKER_TYPE = 2;
    public final int NODE_POS = 3;

    public final int LENGTH_POS = 4;
    public final int HEADER_LENGTH = 5;


    public int currentFrameGroup = 0;
    public boolean frameGroupPart1Recieved = false;
    public boolean frameGroupPart2Recieved = false;
    public byte[] frameSlot1;
    public byte[] frameSlot2;

    DatagramSocket socket;
    Listener listener;
    CountDownLatch latch;

    Node() {
        latch = new CountDownLatch(1);
        listener = new Listener();
        listener.setDaemon(true);
        listener.start();
    }

    public DatagramPacket makePacket(SocketAddress address, byte[] message, byte type, byte frame, byte node, byte numberOfPackets) {
        if (type == TYPE_DATA) {
            byte[] data = new byte[HEADER_LENGTH + message.length];
            data[TYPE_POS] = type;
            data[FRAME_POS] = frame;
            data[NODE_POS] = node;
            data[PACKET_NUMBER_POSITION] = numberOfPackets;
            data[LENGTH_POS] = (byte) message.length;
            System.arraycopy(message, 0, data, HEADER_LENGTH, message.length);
            DatagramPacket packet = new DatagramPacket(data, data.length);
            packet.setSocketAddress(address);
            return packet;
        } else {
            byte[] header = new byte[HEADER_LENGTH];
            header[TYPE_POS] = type;
            header[FRAME_POS] = frame;
            header[PACKET_NUMBER_POSITION] = numberOfPackets;
            header[NODE_POS] = node;
            header[LENGTH_POS] = 0;
            DatagramPacket packet = new DatagramPacket(header, header.length);
            packet.setSocketAddress(address);
            return packet;
        }
    }

    public void splitPacketIntoSmallerPackets(byte[] command, ArrayList<DatagramPacket> commandPackets, SocketAddress brokerAddress, byte nodeType){
        int index = 0;
        int frameIndex = 0;
        int numOfPackets = (command.length / (256 - HEADER_LENGTH)) + 1;
        System.out.println("Num of packets " + numOfPackets);
        byte[] message;
        while (index < command.length){

            if (index + 256 - HEADER_LENGTH > command.length){
                message = Arrays.copyOfRange(command, index, command.length);
                System.out.println("Here");
            } else {
                message = Arrays.copyOfRange(command, index, index + 256 - HEADER_LENGTH);
            }
            index += (256 - HEADER_LENGTH);
            commandPackets.add(makePacket(brokerAddress, message, TYPE_DATA, (byte) (frameIndex % 4), nodeType, (byte) numOfPackets));
            frameIndex++;
        }
    }

    public void commandPacketFraming(byte[] data, SocketAddress returnAddress, ArrayList<byte[]> commandPackets, Terminal terminal) throws Exception {
        if (commandPackets.isEmpty()) {
            currentFrameGroup = 0;
        }
        terminal.println(Integer.toString(data[FRAME_POS]));
        if (currentFrameGroup == 0) {
            if (data[FRAME_POS] == FRAME_1 && frameSlot1 == null) {
                frameSlot1 = data;
                sendAck(returnAddress, FRAME_1);
            } else if (data[FRAME_POS] == FRAME_2 && frameSlot2 == null && frameSlot1 != null) {
                frameSlot2 = data;
                sendAck(returnAddress, FRAME_2);
            } else {
                terminal.println("Wrong packet recieved");
            }
            if (frameSlot1 != null && frameSlot2 != null) {
                currentFrameGroup = 1;
                commandPackets.add(frameSlot1);
                commandPackets.add(frameSlot2);
                frameSlot1 = null;
                frameSlot2 = null;
            }
        } else {
            if (data[FRAME_POS] == FRAME_3 && frameSlot1 == null) {
                frameSlot1 = data;
                sendAck(returnAddress, FRAME_3);
            } else if (data[FRAME_POS] == FRAME_4 && frameSlot2 == null) {
                frameSlot2 = data;
                sendAck(returnAddress, FRAME_4);
            } else {
                terminal.println("Wrong packet recieved");
            }
            if (frameSlot1 != null && frameSlot2 != null) {
                currentFrameGroup = 0;
                commandPackets.add(frameSlot1);
                commandPackets.add(frameSlot2);
                frameSlot1 = null;
                frameSlot2 = null;
            }
        }
        if (data[PACKET_NUMBER_POSITION] - 1 == commandPackets.size() && frameSlot1 != null) {
            commandPackets.add(frameSlot1);
            frameSlot1 = null;
        }
    }

    public byte[] putCommandPacketsTogether(ArrayList<byte[]> commandPackets) {

        byte[] command = new byte[commandPackets.size() * (256)];
        int index = 0;
        for (byte[] part : commandPackets) {
            int size = (part[LENGTH_POS] < 0) ? part[LENGTH_POS] + 256 : part[LENGTH_POS];
            System.arraycopy(part, HEADER_LENGTH, command, index, size);
            index += 256 - HEADER_LENGTH;
        }
        return command;
    }

    public void dealWithAckFrame(byte[] data, Terminal terminal) {
        terminal.println(Integer.toString(data[FRAME_POS]));
        if (currentFrameGroup == 0) {
            if (data[FRAME_POS] == FRAME_1) {
                frameGroupPart1Recieved = true;
            } else if (data[FRAME_POS] == FRAME_2) {
                frameGroupPart2Recieved = true;
            } else {
                terminal.println("Wrong packet recieved");
            }
            if (frameGroupPart1Recieved && frameGroupPart2Recieved){
                currentFrameGroup = 1;
            }
        } else {
            if (data[FRAME_POS] == FRAME_3) {
                frameGroupPart1Recieved = true;
            } else if (data[FRAME_POS] == FRAME_4) {
                frameGroupPart2Recieved = true;
            } else {
                terminal.println("Wrong packet recieved");
            }
            if (frameGroupPart1Recieved && frameGroupPart2Recieved){
                currentFrameGroup = 0;
            }
        }
    }

    public abstract void sendMessage() throws Exception;

    public abstract void sendAck(SocketAddress returnAddress, byte frameNumber) throws Exception;

    public abstract void onReceipt(DatagramPacket packet);

    public abstract void run();

    public abstract void connectToServer() throws Exception;

    class Listener extends Thread {
        public void go() {
            latch.countDown();
        }

        public void run() {
            try {
                latch.await();

                while (true) {
                    DatagramPacket packet = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
                    socket.receive(packet);

                    onReceipt(packet);
                }
            } catch (Exception e) {
                if (!(e instanceof SocketException))
                    e.printStackTrace();
            }
        }
    }

}
