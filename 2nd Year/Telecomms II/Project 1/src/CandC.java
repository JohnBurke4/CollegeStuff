import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class CandC extends Node {

    private final int BROKER_SOCKET = 45000;
    private final String BROKER_NODE = "localhost";


    private boolean connected = false;

    private ArrayList<DatagramPacket> commandPackets = new ArrayList<DatagramPacket>();

    InetSocketAddress brokerAddress;
    Terminal terminal;

    public CandC(Terminal terminal, int socket) {
        try {
            brokerAddress = new InetSocketAddress(BROKER_NODE, BROKER_SOCKET);
            this.socket = new DatagramSocket(socket);
            listener.go();
            this.terminal = terminal;
            connectToServer();
            sendMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public void splitPacketIntoSmallerPackets(byte[] command){
//        int index = 0;
//        int frameIndex = 0;
//        int numOfPackets = (command.length / (256 - HEADER_LENGTH)) + 1;
//        System.out.println("Num of packets " + numOfPackets);
//        byte[] message;
//        while (index < command.length){
//
//            if (index + 256 - HEADER_LENGTH > command.length){
//                message = Arrays.copyOfRange(command, index, command.length);
//                System.out.println("Here");
//            } else {
//                message = Arrays.copyOfRange(command, index, index + 256 - HEADER_LENGTH);
//            }
//            index += (256 - HEADER_LENGTH);
//            commandPackets.add(makePacket(brokerAddress, message, TYPE_DATA, (byte) (frameIndex % 4), C_AND_C_TYPE, (byte) numOfPackets));
//            frameIndex++;
//        }
//    }

//    private void putCommandPacketsTogether() {
//        byte[] command = new byte[commandPackets.size() * (256)];
//        int index = 0;
//        for (DatagramPacket part: commandPackets){
//            byte[] data = part.getData();
//            int size = (data[LENGTH_POS] < 0)?data[LENGTH_POS] + 256:data[LENGTH_POS];
//            System.arraycopy(data, HEADER_LENGTH, command, index, size);
//            index += 256 - HEADER_LENGTH;
//        }
//        BrokerCommand stuff = BrokerCommand.makeFromSerialized(command);
//        terminal.println(stuff.getCommand());
//
//
//    }

    @Override
    public synchronized void sendMessage() throws Exception {
        byte[] command = BrokerCommand.getSerialized(createCommand());
        splitPacketIntoSmallerPackets(command, commandPackets, brokerAddress, C_AND_C_TYPE);
        terminal.println(Integer.toString(commandPackets.size()));
        for (int i = 0; i < commandPackets.size(); i++){
            //terminal.println("Sending");
            socket.send(commandPackets.get(i));
            this.wait(2);
            if (i % 2 == 1){
                while (!frameGroupPart1Recieved || !frameGroupPart2Recieved){
                    terminal.println(Boolean.toString(frameGroupPart1Recieved));
                    terminal.println(Boolean.toString(frameGroupPart2Recieved));
                    terminal.println("Sending packets again");
                    wait(2);
                    socket.send(commandPackets.get(i));
                    socket.send(commandPackets.get(i-1));
                }
                frameGroupPart1Recieved = false;
                frameGroupPart2Recieved = false;
            }

        }
        terminal.println("Sent command");
    }

//    public void dealWithAckFrame(byte[] data) {
//        if (currentFrameGroup == 0) {
//            if (data[FRAME_POS] == FRAME_1) {
//                frameGroupPart1Recieved = true;
//            } else if (data[FRAME_POS] == FRAME_2) {
//                frameGroupPart2Recieved = true;
//            } else {
//                terminal.println("Wrong packet recieved");
//            }
//            if (frameGroupPart1Recieved && frameGroupPart2Recieved){
//                currentFrameGroup = 1;
//            }
//        } else {
//            if (data[FRAME_POS] == FRAME_3) {
//                frameGroupPart1Recieved = true;
//            } else if (data[FRAME_POS] == FRAME_4) {
//                frameGroupPart2Recieved = true;
//            } else {
//                terminal.println("Wrong packet recieved");
//            }
//            if (frameGroupPart1Recieved && frameGroupPart2Recieved){
//                currentFrameGroup = 0;
//            }
//        }
//    }

    public BrokerCommand createCommand() {
        terminal.println("Please issue the command you wish to send: ");
        String command = terminal.read("Command");
        terminal.println("Please input the amount of workers needed: ");
        int workers = Integer.parseInt(terminal.read("Workers:"));
        BrokerCommand commandToSend = new BrokerCommand(command, null, workers);
        commandToSend.setPadding(2000);
        return commandToSend;
    }


    @Override
    public synchronized void sendAck(SocketAddress returnAddress, byte frameNumber) throws Exception {
        socket.send(makePacket(returnAddress, null, TYPE_ACK, frameNumber, C_AND_C_TYPE, HEADER_PACKET_COUNT));
    }

    @Override
    public synchronized void onReceipt(DatagramPacket packet) {
        try {
            byte[] data = packet.getData();
            switch (data[TYPE_POS]) {
                case TYPE_DATA:
                    byte[] byteContent = new byte[data.length - HEADER_LENGTH];
                    System.arraycopy(data, HEADER_LENGTH, byteContent, 0, data.length - HEADER_LENGTH);
                    switch (data[NODE_POS]) {
                        case BROKER_TYPE:
                            BrokerCommand command = BrokerCommand.makeFromSerialized(byteContent);
                            if (command.getComplete()) {
                                terminal.println("The command to | " + command.getCommand() + " | is complete");
                                sendMessage();
                            } else {
                                terminal.println("Something went wrong in trying to complete your command");
                            }
                            break;
                    }
                    sendAck(packet.getSocketAddress(), data[FRAME_POS]);
                    break;
                case TYPE_ACK:
                    //terminal.println("Ack recieved");
                    //terminal.println("Frame: " + data[FRAME_POS]);
                    dealWithAckFrame(data, terminal);
                    break;
                case TYPE_CONNECTION_ACK:
                    terminal.println("Connected to server");
                    connected = true;
                    break;
                default:
                    terminal.println("Error, wrong data type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public synchronized void connectToServer() throws Exception {
        while (!connected) {
            socket.send(makePacket(brokerAddress, null, TYPE_CONNECTION, FRAME_1, C_AND_C_TYPE, HEADER_PACKET_COUNT));
            terminal.println("No connection yet, trying again...");
            this.wait(3000);

        }
    }

    public static void main(String[] args) {
        try {
            Terminal terminal = new Terminal("Command & Control");
            CandC candc = new CandC(terminal, 50010);
            candc.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
