import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;


public class Broker extends Node {

    private final int BROKER_SOCKET = 45000;
    private final String BROKER_NODE = "localhost";
    private BrokerCommand currentOrder = null;

    private ArrayList<BrokerCommand> backlog = new ArrayList<BrokerCommand>();

    InetSocketAddress brokerAddress;

    ArrayList<SocketAddress> workerAddresses = new ArrayList<SocketAddress>();

    private SocketAddress cAndCAddress;

    public void setCurrentOrder(BrokerCommand order) {
        currentOrder = order;
    }

    public BrokerCommand getCurrentOrder() {
        return currentOrder;
    }


    public Broker() {
        try {
            brokerAddress = new InetSocketAddress(BROKER_NODE, BROKER_SOCKET);
            this.socket = new DatagramSocket(BROKER_SOCKET);
            listener.go();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public synchronized void sendMessage() throws Exception {
        BrokerCommand command = getCurrentOrder();
        BrokerCommand.WorkerCommand commandToSend = command.createWorkerCommand();
        byte[] message = BrokerCommand.getWorkerSerializable(commandToSend);
        for (SocketAddress currentWorkerAddress : workerAddresses) {
            socket.send(makePacket(currentWorkerAddress, message, TYPE_DATA, FRAME_1, BROKER_TYPE));
        }
    }

    @Override
    public synchronized void sendAck(SocketAddress returnAddress) throws Exception {
        socket.send(makePacket(returnAddress, null, TYPE_ACK, FRAME_1, BROKER_TYPE));
    }

    @Override
    public synchronized void onReceipt(DatagramPacket packet) {
        // TODO Auto-generated method stub
        try {
            String content;
            byte[] data = packet.getData();
            switch (data[TYPE_POS]) {
                case TYPE_DATA:
                    byte[] byteContent = new byte[data.length - HEADER_LENGTH];
                    System.arraycopy(data, HEADER_LENGTH, byteContent, 0, data.length - HEADER_LENGTH);
                    switch (data[NODE_POS]) {
                        case WORKER_TYPE:
                            BrokerCommand.WorkerCommand workerResponse = BrokerCommand.makeWorkerFromSerialized(byteContent);
                            if (workerResponse.getComplete()) {
                                getCurrentOrder().incrementNumberComplete();
                                System.out.println("Finished");
                                getCurrentOrder().setComplete();
                                if (getCurrentOrder().getComplete()){
									System.out.println("Is complete");
									sendCommandBackToCAndC(getCurrentOrder());
								}
                            } else if (workerResponse.getAccepted()) {
                                getCurrentOrder().incrememtNumberAccepted();
                                System.out.println("Accepted");
                            } else {
                                System.out.println("Declined");
                                backlog.add(getCurrentOrder());
                            }
                            break;
                        case C_AND_C_TYPE:
                            BrokerCommand command = BrokerCommand.makeFromSerialized(byteContent);
                            setCurrentOrder(command);
                            getCurrentOrder().setSender(packet.getSocketAddress());
                            System.out.println(command.getCommand());
                            sendMessage();
                            break;
                    }

                    sendAck(packet.getSocketAddress());
                    break;
                case TYPE_ACK:
                    System.out.println("Ack recieved");
                    break;

                case TYPE_CONNECTION:
                    System.out.println("Connection recieved: ");
                    switch (data[NODE_POS]) {
                        case WORKER_TYPE:
                            System.out.println("Worker connected");
                            if (workerAddresses.contains(packet.getSocketAddress())) {
                            } else {
                                workerAddresses.add(packet.getSocketAddress());
                            }
                            break;
                        case C_AND_C_TYPE:
                            System.out.println("C and C connected");
                            cAndCAddress = packet.getSocketAddress();
                            break;
                    }
                    sendConnectionAck(packet.getSocketAddress());
                    break;
                default:
                    System.out.println("Error, wrong data type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        while (true) {

        }
    }

    public void sendCommandBackToCAndC(BrokerCommand command) throws IOException {
		byte[] message = BrokerCommand.getSerialized(command);
        socket.send(makePacket(command.getSender(), message, TYPE_DATA, FRAME_1, BROKER_TYPE));
		System.out.println("Sent command back");

	}


    @Override
    public void connectToServer() throws Exception {

    }

    public void sendConnectionAck(SocketAddress address) throws Exception {
        socket.send(makePacket(address, null, TYPE_CONNECTION_ACK, FRAME_1, BROKER_TYPE));
    }

    public static void main(String[] args) {
        try {
            Broker broker = new Broker();
            broker.run();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

}
