import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Worker extends Node {

    private final int BROKER_SOCKET = 45000;
    private final String BROKER_NODE = "localhost";

    InetSocketAddress brokerAddress;

    private boolean connected = false;
    private WorkerCommand currentCommand = null;
    Terminal terminal;
    private boolean orderReceived = false;

    public ArrayList<byte[]> incomingPackets = new ArrayList<byte[]>();
    public ArrayList<DatagramPacket> outgoingPackets = new ArrayList<DatagramPacket>();


    public Worker(Terminal terminal, int socket) {
        try {
            this.terminal = terminal;
            brokerAddress = new InetSocketAddress(BROKER_NODE, BROKER_SOCKET);
            this.socket = new DatagramSocket(socket);
            listener.go();
            connectToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public synchronized void sendMessage() throws Exception {

    }

    @Override
    public synchronized void sendAck(SocketAddress returnAddress, byte frameNumber) throws Exception {
        socket.send(makePacket(returnAddress, null, TYPE_ACK, frameNumber, WORKER_TYPE, HEADER_PACKET_COUNT));
        terminal.println("Ack sent");
    }

    @Override
    public synchronized void onReceipt(DatagramPacket packet) {
        try {
            byte[] data = packet.getData();
            switch (data[TYPE_POS]) {
                case TYPE_DATA:
                    if (Math.random() > PACKET_LOSS_PERCENT) {
                        commandPacketFraming(data, packet.getSocketAddress(), incomingPackets, terminal);
                        terminal.println("Got something");
                    }
                    if (incomingPackets.size() == data[PACKET_NUMBER_POSITION]) {
                        terminal.println("All packets recieved");
                        orderReceived = true;
                    }
                    break;
                case TYPE_ACK:
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

    public void acceptOrDeclineOrder() {

        boolean correctInput = false;
        boolean accepted = true;
        while (!correctInput) {
            terminal.println("Would you like to accept this order? [Y:N]");
            String input = terminal.read(" Y:N ");
            if (input.equals("Y")) {
                correctInput = true;
                accepted = true;
            } else if (input.equals("N")) {
                accepted = false;
                correctInput = true;
            } else {
                terminal.println("Incorrect input, please try again");
                continue;
            }
            try {
                sendOrderReply(accepted);
                if (accepted) {
                    sayIfFinished();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void sendOrderReply(boolean accepted) throws Exception {
        currentCommand.setAccepted(accepted);
        byte[] command = WorkerCommand.getWorkerSerializable(currentCommand);
        if (!currentCommand.getAccepted()) {
            terminal.println("Sending refusal");
        }
        splitPacketIntoSmallerPackets(command, outgoingPackets, brokerAddress, WORKER_TYPE);
        for (int i = 0; i < outgoingPackets.size(); i++) {
            socket.send(outgoingPackets.get(i));
            this.wait(4);
            if (i % 2 == 1) {
                while (!frameGroupPart1Recieved || !frameGroupPart2Recieved) {
                    terminal.println("Sending packets again");
                    wait(2);
                    socket.send(outgoingPackets.get(i));
                    socket.send(outgoingPackets.get(i - 1));
                }
                frameGroupPart1Recieved = false;
                frameGroupPart2Recieved = false;
            }

        }
        frameGroupPart1Recieved = false;
        frameGroupPart2Recieved = false;
        terminal.println("Sent command");
        outgoingPackets.clear();
        currentFrameGroup = 0;

    }

    public void sayIfFinished() {

        boolean correctInput = false;
        while (!correctInput) {
            terminal.println("Enter [F] if finished");
            String input = terminal.read(" F ");
            if (input.equals("F")) {
                correctInput = true;
                currentCommand.setComplete(true);
            } else {
                terminal.println("Current Order: " + currentCommand.getCommand());
                continue;
            }
            try {
                sendOrderReply(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void run() {
        try {
            while (true) {
                this.wait(100);
                if (orderReceived){
                    WorkerCommand command = WorkerCommand.makeWorkerFromSerialized(putCommandPacketsTogether(incomingPackets));
                    terminal.println("Command said: " + command.getCommand());
                    currentCommand = command;
                    incomingPackets.clear();
                    sendMessage();
                    acceptOrDeclineOrder();
                    orderReceived = false;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void connectToServer() throws Exception {
        while (!connected) {
            socket.send(makePacket(brokerAddress, null, TYPE_CONNECTION, FRAME_1, WORKER_TYPE, HEADER_PACKET_COUNT));
            terminal.println("No connection yet, trying again...");
            this.wait(3000);
        }
    }


}
