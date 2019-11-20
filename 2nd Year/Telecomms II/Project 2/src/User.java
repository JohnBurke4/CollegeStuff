import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class User extends Node {

    InetSocketAddress controlAddress;
    boolean connected = false;
    Terminal terminal;
    RouterFlowTable flowTable;
    InetSocketAddress nearestRouter;
    Integer destination;
    boolean sendMessage;
    boolean canSendMessage = false;

    public User(int address, Terminal terminal, boolean sendMessage) {
        try {
            this.terminal = terminal;
            this.socket = new DatagramSocket(address);
            this.listener.go();
            controlAddress = new InetSocketAddress("localhost", CONTROL_ADDRESS);
            flowTable = new RouterFlowTable();
            this.sendMessage = sendMessage;
            connectToControl();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNearestRouter(int port) {
        nearestRouter = new InetSocketAddress("localhost", port);
    }

    public void setDestination(int port) {
        destination = port;
    }

    @Override
    public void sendMessage(SocketAddress address, String message, byte type) {
        try {
            byte[] serial = message.getBytes();
            socket.send(makePacket(address, serial, type));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendAck(SocketAddress returnAddress) throws Exception {
        socket.send(makePacket(returnAddress, null, TYPE_ACK));
    }

    @Override
    public synchronized void onReceipt(DatagramPacket packet)  {
        byte[] data = packet.getData();
        switch (data[TYPE_POS]) {
            case TYPE_HELLO:
                terminal.println("Connected to Control");
                connected = true;
                try {
                    wait(1000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                if (sendMessage){
                    canSendMessage = true;
                    //sendMessageToUser();
                }

                break;
            case TYPE_DATA:
                printMessage(data);
                sendMessage = true;
                canSendMessage = true;
                break;
            default:
                terminal.println("Unknown packet type recieved");
        }
    }

    public void printMessage(byte[] data){
        byte[] message = new byte[data[LENGTH_POS]];
        System.arraycopy(data, HEADER_LENGTH, message, 0, data[LENGTH_POS]);
        String messageString = new String(message);
        System.out.println(messageString);
        String[] split = messageString.split("\\|");
        terminal.println(split[0]);
    }

    public void sendMessageToUser() {
        terminal.println("Please input your message:");
        String message = terminal.read(" M: ");
        sendMessage(nearestRouter, message + "|" + destination , TYPE_DATA);
        System.out.println("Sent message");
    }

    @Override
    public synchronized void run() {
        while (true){
            try {
                wait(200);
                if (canSendMessage){
                    sendMessageToUser();
                    canSendMessage = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public synchronized void connectToControl() throws Exception {
        socket.send(makePacket(controlAddress, null, TYPE_HELLO));
        wait(100);
        while (!connected) {
            terminal.println("No connection, trying again...");
            socket.send(makePacket(controlAddress, null, TYPE_HELLO));
            wait(3000);
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(User::createAndRunUser1);
        executorService.submit(User::createAndRunUser2);
        executorService.shutdown();
    }

    public static void createAndRunUser1() {
        Terminal terminal = new Terminal("User 1");
        User user = new User(40010, terminal, true);
        user.setNearestRouter(40012);
        user.setDestination(40011);
        user.run();
    }

    public static void createAndRunUser2() {
        Terminal terminal = new Terminal("User 2");
        User user = new User(40011, terminal, false);
        user.setNearestRouter(40015);
        user.setDestination(40010);
        user.run();
    }
}
