import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;

public class CandC extends Node {

    private final int BROKER_SOCKET = 45000;
    private final String BROKER_NODE = "localhost";


    private boolean connected = false;
    private boolean commandRecieved = false;

    private ArrayList<DatagramPacket> commandPackets = new ArrayList<DatagramPacket>();
    private ArrayList<byte[]> incomingPackets = new ArrayList<byte[]>();

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

    @Override
    public synchronized void sendMessage() throws Exception {
        byte[] command = BrokerCommand.getSerialized(createCommand());
        splitPacketIntoSmallerPackets(command, commandPackets, brokerAddress, C_AND_C_TYPE);
        for (int i = 0; i < commandPackets.size(); i++) {
            socket.send(commandPackets.get(i));
            this.wait(2);
            if (i % 2 == 1) {
                while (!frameGroupPart1Recieved || !frameGroupPart2Recieved) {
                    terminal.println("Sending packets again");
                    wait(4);
                    socket.send(commandPackets.get(i));
                    socket.send(commandPackets.get(i - 1));
                }
                frameGroupPart1Recieved = false;
                frameGroupPart2Recieved = false;
            }

        }
        resetFraming();
        terminal.println("Sent command");
    }

    public void resetFraming() {
        commandPackets.clear();
        currentFrameGroup = 0;
    }

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
                            // simulates packetloss
                            if (Math.random() > PACKET_LOSS_PERCENT) {
                                commandPacketFraming(data, packet.getSocketAddress(), incomingPackets, terminal);
                            }
                            if (incomingPackets.size() == data[PACKET_NUMBER_POSITION]) {
                                terminal.println("All packets recieved");
                                commandRecieved = true;
                            }

                            break;
                        default:
                            terminal.println("Error in Node who sent message, wrong node type;");
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

    @Override
    public synchronized void run() {
        while (true) {
            try {
                wait(100);
                if (commandRecieved) {
                    BrokerCommand command = BrokerCommand.makeFromSerialized(putCommandPacketsTogether(incomingPackets));
                    incomingPackets.clear();
                    if (command.getComplete()) {
                        terminal.println("The command to | " + command.getCommand() + " | is complete");
                        sendMessage();
                    } else {
                        terminal.println("Something went wrong in trying to complete your command");
                    }
                    commandRecieved = false;
                }
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
