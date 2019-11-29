import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class User extends Node {

    InetSocketAddress controlAddress;
    boolean connected = false;
    Terminal terminal;
    RouterFlowTable flowTable;
    InetSocketAddress nearestRouter;
    boolean sendMessage;
    boolean canSendMessage = false;
    int address;

    static HashMap<String, Integer> Users = new HashMap<>();

    public User(int address, Terminal terminal, boolean sendMessage) {
        try {
            this.terminal = terminal;
            this.address = address;
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
            case TYPE_FEATURE_RESPONSE:
                terminal.println("Connected to Control");
                connected = true;
                setNearestRouter(getConnectedRouter(data));
                try {
                    wait(1000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                if (sendMessage){
                    canSendMessage = true;
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

    public int getConnectedRouter(byte[] data){
        byte[] message = new byte[data[LENGTH_POS]];
        System.arraycopy(data, HEADER_LENGTH, message, 0, data[LENGTH_POS]);
        String messageString = new String(message);
        return Integer.parseInt(messageString);
    }

    public void printMessage(byte[] data){
        byte[] message = new byte[data[LENGTH_POS]];
        System.arraycopy(data, HEADER_LENGTH, message, 0, data[LENGTH_POS]);
        String messageString = new String(message);
        String[] split = messageString.split("\\|");
        terminal.println(split[0]);
    }

    public void sendMessageToUser() {
        terminal.println("Please input your message:");
        String message = terminal.read(" M: ");
        boolean validDestination = false;
        String destinationName = "";
        while (!validDestination){
            terminal.println("Available Users");
            for (String user: Users.keySet()){
                if (!user.equals(terminal.name)){
                    terminal.println(user);
                }
            }
            terminal.println("Please input destination name: ");
            destinationName = terminal.read(" D: ");
            if (Users.containsKey(destinationName)){
                validDestination = true;
            } else{
                terminal.println("Invalid User, please try again");
            }
        }
        sendMessage(nearestRouter, message + "|" + Users.get(destinationName) , TYPE_DATA);
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
        socket.send(makePacket(controlAddress, null, TYPE_USER_HELLO));
        Users.put(terminal.name, address);
        wait(100);
        while (!connected) {
            terminal.println("No connection, trying again...");
            socket.send(makePacket(controlAddress, null, TYPE_USER_HELLO));
            wait(3000);
        }
    }

    public synchronized static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(User::createAndRunUser1);
        executorService.submit(User::createAndRunUser2);
        executorService.shutdown();
    }

    public static void createAndRunUser1() {
        Terminal terminal = new Terminal("User1");
        User user = new User(40001, terminal, true);
        user.run();
    }

    public static void createAndRunUser2() {
        Terminal terminal = new Terminal("User2");
        User user = new User(40002, terminal, false);
        user.run();
    }
}
