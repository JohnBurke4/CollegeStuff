import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Random;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;


public class Broker extends Node {

    private final int BROKER_SOCKET = 45000;
    private final String BROKER_NODE = "localhost";
    private BrokerCommand currentOrder = null;

    private ArrayList<BrokerCommand> backlog = new ArrayList<BrokerCommand>();

    InetSocketAddress brokerAddress;

    ArrayList<SocketAddress> workerAddresses = new ArrayList<SocketAddress>();

    ArrayList<SocketAddress> workersSentTask = new ArrayList<SocketAddress>();

    ArrayList<byte[]> commandPackets = new ArrayList<byte[]>();

    private SocketAddress cAndCAddress;


    private int currentFrameGroup = 0;
    private byte[] frameSlot1;
    private byte[] frameSlot2;

    public void setCurrentOrder(BrokerCommand order) {
        currentOrder = order;
    }

    public BrokerCommand getCurrentOrder() {
        return currentOrder;
    }

    Terminal terminal;

    public Broker(Terminal terminal) {
        try {
            brokerAddress = new InetSocketAddress(BROKER_NODE, BROKER_SOCKET);
            this.socket = new DatagramSocket(BROKER_SOCKET);
            listener.go();
            this.terminal = terminal;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public synchronized void sendMessage() throws Exception {
        BrokerCommand command = getCurrentOrder();
        WorkerCommand commandToSend = new WorkerCommand(command.getCommand());
        byte[] message = WorkerCommand.getWorkerSerializable(commandToSend);
        byte[] message2 = BrokerCommand.getSerialized(command);
        System.out.println(message.length);
        System.out.println(message2.length);
        for (SocketAddress currentWorkerAddress : workerAddresses) {
            if (!workersSentTask.contains(currentWorkerAddress)) {
                System.out.println(message.length);
                socket.send(makePacket(currentWorkerAddress, message, TYPE_DATA, FRAME_1, BROKER_TYPE, HEADER_PACKET_COUNT));
                command.incrementNumberOrdersSent();
                workersSentTask.add(currentWorkerAddress);
                if (command.checkIfEnoughOrdersSent()) {
                    terminal.println("EnoughOrdersSent");
                    break;
                }
            }
        }
    }

    @Override
    public synchronized void sendAck(SocketAddress returnAddress, byte frameNumber) throws Exception {
        socket.send(makePacket(returnAddress, null, TYPE_ACK, frameNumber, BROKER_TYPE, HEADER_PACKET_COUNT));
    }

    @Override
    public synchronized void onReceipt(DatagramPacket packet) {
        try {
            String content;
            byte[] data = packet.getData();
            switch (data[TYPE_POS]) {
                case TYPE_DATA:
                    byte[] byteContent = new byte[data.length - HEADER_LENGTH];
                    System.arraycopy(data, HEADER_LENGTH, byteContent, 0, data.length - HEADER_LENGTH);
                    switch (data[NODE_POS]) {
                        case WORKER_TYPE:
                            WorkerCommand workerResponse = WorkerCommand.makeWorkerFromSerialized(byteContent);
                            if (workerResponse.getComplete() && !getCurrentOrder().getComplete()) {
                                getCurrentOrder().incrementNumberComplete();
                                terminal.println("Finished");
                                workersSentTask.remove(packet.getSocketAddress());
                                getCurrentOrder().decrementNumberOrdersSent();
                                getCurrentOrder().setComplete();
                                if (getCurrentOrder().getComplete()) {
                                    terminal.println("Is complete");
                                    workersSentTask = new ArrayList<SocketAddress>();
                                    sendCommandBackToCAndC(getCurrentOrder());
                                } else if (getCurrentOrder().getWorkersNeeded() != 0) {
                                    terminal.println("Not enough workers, sending same task again");
                                    sendMessage();
                                }
                            } else if (workerResponse.getAccepted() && !workerResponse.getComplete()) {
                                getCurrentOrder().incrememtNumberAccepted();
                                terminal.println("Accepted");
                            } else if (!workerResponse.getAccepted() && !getCurrentOrder().getComplete()) {
                                terminal.println("Declined");
                                getCurrentOrder().decrementNumberOrdersSent();
                                sendMessage();
                                workersSentTask.remove(packet.getSocketAddress());
                                backlog.add(getCurrentOrder());
                            } else {
                                terminal.println("Problem with incoming response, discarding it");
                            }
                            break;
                        case C_AND_C_TYPE:
                            if (Math.random() > 0.0) {
                                commandPacketFraming(data, packet.getSocketAddress());
                                terminal.println(Integer.toString(commandPackets.size()));
                            }
                            if (commandPackets.size() == data[PACKET_NUMBER_POSITION]) {
                                terminal.println("All packets recieved");
                                BrokerCommand command = BrokerCommand.makeFromSerialized(putCommandPacketsTogether(commandPackets));
                                setCurrentOrder(command);
                                getCurrentOrder().setSender(packet.getSocketAddress());
                                terminal.println(getCurrentOrder().getCommand());
                                commandPackets = new ArrayList<byte[]>();
                                sendMessage();
                            }
                    }
                    break;
                case TYPE_ACK:
                    terminal.println("Ack recieved");
                    break;

                case TYPE_CONNECTION:
                    terminal.println("Connection recieved: ");
                    switch (data[NODE_POS]) {
                        case WORKER_TYPE:
                            terminal.println("Worker connected");
                            if (workerAddresses.contains(packet.getSocketAddress())) {
                            } else {
                                workerAddresses.add(packet.getSocketAddress());
                            }
                            break;
                        case C_AND_C_TYPE:
                            terminal.println("C and C connected");
                            cAndCAddress = packet.getSocketAddress();
                            break;
                    }
                    sendConnectionAck(packet.getSocketAddress());
                    break;
                default:
                    terminal.println("Error, wrong data type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

//    private BrokerCommand putCommandPacketsTogether() {
//
//        byte[] command = new byte[commandPackets.size() * (256)];
//        int index = 0;
//        for (byte[] part : commandPackets) {
//            int size = (part[LENGTH_POS] < 0) ? part[LENGTH_POS] + 256 : part[LENGTH_POS];
//            // System.out.println(data.length);
//            // System.out.println(size);
//            System.arraycopy(part, HEADER_LENGTH, command, index, size);
//            index += 256 - HEADER_LENGTH;
//        }
//        BrokerCommand result = BrokerCommand.makeFromSerialized(command);
//        return result;
//
//
//    }


    public void commandPacketFraming(byte[] data, SocketAddress returnAddress) throws Exception {
        if (commandPackets.isEmpty()) {
            currentFrameGroup = 0;
            //terminal.println("FrameGroup = 0");
        }

        //terminal.println("Doing stuff");
        terminal.println(Byte.toString(data[FRAME_POS]) + " " + Byte.toString(FRAME_2));
        if (currentFrameGroup == 0) {
            terminal.println(Byte.toString(data[FRAME_POS]) + " " + Byte.toString(FRAME_2));
            if (data[FRAME_POS] == FRAME_1 && frameSlot1 == null) {
                //terminal.println("Dataframe 1");
                frameSlot1 = data;
                sendAck(returnAddress, FRAME_1);
            } else if (data[FRAME_POS] == FRAME_2 && frameSlot2 == null && frameSlot1 != null) {
                //terminal.println("Dataframe 2");
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
        }
    }

    @Override
    public void run() {
        while (true) {

        }
    }

    public void sendCommandBackToCAndC(BrokerCommand command) throws IOException {
        byte[] message = BrokerCommand.getSerialized(command);
        socket.send(makePacket(command.getSender(), message, TYPE_DATA, FRAME_1, BROKER_TYPE, HEADER_PACKET_COUNT));
        terminal.println("Sent command back");

    }


    @Override
    public void connectToServer() throws Exception {

    }

    public void sendConnectionAck(SocketAddress address) throws Exception {
        socket.send(makePacket(address, null, TYPE_CONNECTION_ACK, FRAME_1, BROKER_TYPE, HEADER_PACKET_COUNT));
    }

    public static void main(String[] args) {
        try {
            Terminal terminal = new Terminal("Broker");
            Broker broker = new Broker(terminal);
            broker.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
