import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
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
        socket.send(makePacket(returnAddress, null, TYPE_ACK, FRAME_1, WORKER_TYPE, HEADER_PACKET_COUNT));
        terminal.println("Ack sent");
    }

    @Override
    public synchronized void onReceipt(DatagramPacket packet) {
        try {
            byte[] data = packet.getData();
            switch (data[TYPE_POS]) {
                case TYPE_DATA:
                    byte[] byteContent = new byte[data.length - HEADER_LENGTH];
                    System.arraycopy(data, HEADER_LENGTH, byteContent, 0, data.length - HEADER_LENGTH);
                    WorkerCommand command = WorkerCommand.makeWorkerFromSerialized(byteContent);
                    terminal.println("Command said: " + command.getCommand());
                    currentCommand = command;
                    sendAck(packet.getSocketAddress(), data[FRAME_POS]);
                    acceptOrDeclineOrder();
                    break;
                case TYPE_ACK:
                    terminal.println("Ack recieved");
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

    public synchronized void sendOrderReply(boolean accepted) throws IOException {
        currentCommand.setAccepted(accepted);
        byte[] command = WorkerCommand.getWorkerSerializable(currentCommand);
        if (!currentCommand.getAccepted()) {
            terminal.println("Sending refusal");
        }
        socket.send(makePacket(brokerAddress, command, TYPE_DATA, FRAME_1, WORKER_TYPE, HEADER_PACKET_COUNT));
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
    public void run() {
        while (true) {
            try {
                //sendMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
