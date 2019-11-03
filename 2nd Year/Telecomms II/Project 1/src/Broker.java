import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.ArrayList;

public class Broker extends Node {

    private final int BROKER_SOCKET = 45000;
    private BrokerCommand currentOrder = null;
    private boolean commandMessageRecieved = false;
    private boolean workerMessageRecieved = false;


    ArrayList<SocketAddress> workerAddresses = new ArrayList<SocketAddress>();

    ArrayList<SocketAddress> workersSentTask = new ArrayList<SocketAddress>();

    ArrayList<byte[]> commandPackets = new ArrayList<byte[]>();
    ArrayList<byte[]> incomingWorkerPackets = new ArrayList<byte[]>();

    ArrayList<DatagramPacket> outgoingPackets = new ArrayList<DatagramPacket>();

    private SocketAddress cAndCAddress;
    private SocketAddress workerSocket;

    public void setCurrentOrder(BrokerCommand order) {
        currentOrder = order;
    }

    public BrokerCommand getCurrentOrder() {
        return currentOrder;
    }

    Terminal terminal;

    public Broker(Terminal terminal) {
        try {
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
        for (SocketAddress currentWorkerAddress : workerAddresses) {
            if (!workersSentTask.contains(currentWorkerAddress)) {
                splitPacketIntoSmallerPackets(message, outgoingPackets, currentWorkerAddress, BROKER_TYPE);
                for (int i = 0; i < outgoingPackets.size(); i++) {
                    socket.send(outgoingPackets.get(i));
                    this.wait(2);
                    if (i % 2 == 1) {
                        while (!frameGroupPart1Recieved || !frameGroupPart2Recieved) {
                            terminal.println("Sending packets again");
                            wait(4);
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
                command.incrementNumberOrdersSent();
                currentFrameGroup = 0;
                outgoingPackets.clear();
                workersSentTask.add(currentWorkerAddress);
                if (command.checkIfEnoughOrdersSent()) {
                    terminal.println("All orders sent");
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
            byte[] data = packet.getData();
            switch (data[TYPE_POS]) {
                case TYPE_DATA:
                    byte[] byteContent = new byte[data.length - HEADER_LENGTH];
                    System.arraycopy(data, HEADER_LENGTH, byteContent, 0, data.length - HEADER_LENGTH);
                    switch (data[NODE_POS]) {
                        case WORKER_TYPE:
                            // simulates packet loss
                            if (Math.random() > PACKET_LOSS_PERCENT) {
                                commandPacketFraming(data, packet.getSocketAddress(), incomingWorkerPackets, terminal);
                            }
                            if (incomingWorkerPackets.size() == data[PACKET_NUMBER_POSITION]) {
                                terminal.println("All packets recieved");
                                workerMessageRecieved = true;
                                workerSocket = packet.getSocketAddress();
                            }
                            break;
                        case C_AND_C_TYPE:
                            if (Math.random() > 0.0) {
                                commandPacketFraming(data, packet.getSocketAddress(), commandPackets, terminal);
                            }
                            if (commandPackets.size() == data[PACKET_NUMBER_POSITION]) {
                                terminal.println("All packets recieved");
                                BrokerCommand command = BrokerCommand.makeFromSerialized(putCommandPacketsTogether(commandPackets));
                                setCurrentOrder(command);
                                getCurrentOrder().setSender(packet.getSocketAddress());
                                terminal.println(getCurrentOrder().getCommand());
                                commandPackets.clear();
                                commandMessageRecieved = true;
                            }
                    }
                    break;
                case TYPE_ACK:
                    dealWithAckFrame(data, terminal);
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

    @Override
    public synchronized void run() {
        try {
            while (true) {
                wait(100);
                if (workerMessageRecieved) {
                    currentFrameGroup = 0;
                    WorkerCommand workerResponse = WorkerCommand.makeWorkerFromSerialized(putCommandPacketsTogether(incomingWorkerPackets));
                    incomingWorkerPackets.clear();


                    if (workerResponse.getComplete() && !getCurrentOrder().getComplete()) {
                        getCurrentOrder().incrementNumberComplete();
                        terminal.println("Finished");
                        workersSentTask.remove(workerSocket);
                        getCurrentOrder().decrementNumberOrdersSent();
                        getCurrentOrder().setComplete();
                        if (getCurrentOrder().getComplete()) {
                            terminal.println("Is complete");
                            workersSentTask.clear();
                            sendCommandBackToCAndC(getCurrentOrder());
                        } else if (getCurrentOrder().getWorkersNeeded() >= 0) {
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
                        workersSentTask.remove(workerSocket);
                    } else {
                        terminal.println("Problem with incoming response, discarding it");
                    }
                    workerMessageRecieved = false;
                } else if (commandMessageRecieved) {
                    sendMessage();
                    commandMessageRecieved = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendCommandBackToCAndC(BrokerCommand command) throws Exception {
        byte[] message = BrokerCommand.getSerialized(command);
        splitPacketIntoSmallerPackets(message, outgoingPackets, command.getSender(), BROKER_TYPE);
        for (int i = 0; i < outgoingPackets.size(); i++) {
            socket.send(outgoingPackets.get(i));
            this.wait(2);
            if (i % 2 == 1) {
                while (!frameGroupPart1Recieved || !frameGroupPart2Recieved) {
                    terminal.println("Sending packets again");
                    wait(4);
                    socket.send(outgoingPackets.get(i));
                    socket.send(outgoingPackets.get(i - 1));
                }
                frameGroupPart1Recieved = false;
                frameGroupPart2Recieved = false;
            }

        }
        frameGroupPart1Recieved = false;
        frameGroupPart2Recieved = false;
        outgoingPackets.clear();
        currentFrameGroup = 0;
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
