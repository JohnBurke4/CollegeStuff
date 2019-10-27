import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Scanner;

public class CandC extends Node{

    private final int NODE_POS = 2;

    private final int LENGTH_POS = 3;
    private final int HEADER_LENGTH = 4;

    private final int BROKER_SOCKET = 45000;
    private final String BROKER_NODE = "localhost";

    private boolean connected = false;

    InetSocketAddress brokerAddress;



    public CandC(int socket) {
        try {
            brokerAddress = new InetSocketAddress(BROKER_NODE, BROKER_SOCKET);
            this.socket = new DatagramSocket(socket);
            listener.go();
            connectToServer();
            sendMessage();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }



    @Override
    public synchronized void sendMessage() throws Exception {
        // TODO Auto-generated method stub

        byte[] data = null;
        DatagramPacket packet = null;


        byte[] command = BrokerCommand.getSerialized(createCommand());
        data = new byte[command.length + HEADER_LENGTH];
        data[TYPE_POS] = TYPE_DATA;
        data[FRAME_POS] = 0;
        data[NODE_POS] = C_AND_C_TYPE;
        data[LENGTH_POS] = (byte) command.length;
        System.arraycopy(command, 0, data, HEADER_LENGTH, command.length);
        packet = new DatagramPacket(data, data.length);
        packet.setSocketAddress(brokerAddress);
        socket.send(packet);
        System.out.println("Sent command");
        this.wait();
    }

    public BrokerCommand createCommand(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Please issue the command you wish to send: ");
        String command = sc.nextLine();
        System.out.println("Please input the amount of workers needed: ");
        int workers = sc.nextInt();
        BrokerCommand commandToSend = new BrokerCommand(command, null, workers);
        return commandToSend;
    }


    @Override
    public synchronized void sendAck(SocketAddress returnAddress) throws Exception {
        // TODO Auto-generated method stub
        byte[] data = new byte[HEADER_LENGTH];
        DatagramPacket packet = null;
        data[TYPE_POS] = TYPE_ACK;
        data[FRAME_POS] = 0;
        data[NODE_POS] = C_AND_C_TYPE;
        packet = new DatagramPacket(data, data.length);
        packet.setSocketAddress(returnAddress);
        socket.send(packet);
    }
    @Override
    public synchronized void onReceipt(DatagramPacket packet) {
        // TODO Auto-generated method stub
        System.out.println("Got something");
        try {
            String content;
            byte[] data = packet.getData();


            switch (data[TYPE_POS]) {
                case TYPE_DATA:
                    byte[] byteContent = new byte[data.length - HEADER_LENGTH];
                    System.arraycopy(data, HEADER_LENGTH, byteContent, 0, data.length - HEADER_LENGTH);
                    switch( data[NODE_POS]) {
                        case BROKER_TYPE:
                            BrokerCommand command = BrokerCommand.makeFromSerialized(byteContent);
                            if (command.getComplete()){
                                System.out.println("The command to | "+ command.getCommand() + " | is complete");
                                sendMessage();
                            }
                            else {
                                System.out.println("Something went wrong in trying to complete your command");
                            }
                            break;
                    }
                    sendAck(packet.getSocketAddress());
                    break;
                case TYPE_ACK:
                    System.out.println("Ack recieved");
                    break;
                case TYPE_CONNECTION_ACK:
                    System.out.println("Connected to server");
                    connected = true;
                    break;
                default:
                    System.out.println("Error, wrong data type");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        while(true) {
            try {
                //sendMessage();
            }
            catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }


    @Override
    public synchronized void connectToServer() throws Exception {
        while (!connected){
            byte[] data = new byte[HEADER_LENGTH];
            DatagramPacket packet = null;
            data[TYPE_POS] = TYPE_CONNECTION;
            data[FRAME_POS] = 0;
            data[NODE_POS] = C_AND_C_TYPE;
            packet = new DatagramPacket(data, data.length);
            packet.setSocketAddress(brokerAddress);
            socket.send(packet);
            System.out.println("No connection yet, trying again...");
            this.wait(2000);

        }
    }

    public static void main(String[] args) {
        try {
            CandC candc = new CandC(50010);
            candc.run();
        }
        catch (Exception e) {
            // TODO: handle exception
        }
    }

}
