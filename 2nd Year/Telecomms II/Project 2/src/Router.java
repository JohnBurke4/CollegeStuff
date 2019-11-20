import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Router extends Node {

    InetSocketAddress controlAddress;
    boolean connected = false;
    Terminal terminal;
    RouterFlowTable flowTable;
    DatagramPacket currentData = null;



    public Router(int address, Terminal terminal) {
        try {
            this.terminal = terminal;
            this.socket = new DatagramSocket(address);
            this.listener.go();
            controlAddress = new InetSocketAddress("localhost", CONTROL_ADDRESS);
            flowTable = new RouterFlowTable();
            connectToControl();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void onReceipt(DatagramPacket packet) {
        byte[] data = packet.getData();
        switch (data[TYPE_POS]) {
            case TYPE_DATA:
                terminal.println("Message recieved to send on");
                currentData = packet;
                break;
            case TYPE_HELLO:
                terminal.println("Connected to Control");
                connected = true;
                break;
            case TYPE_FEATURE_RESPONSE:
                terminal.println("Route Recieved");
                addRouteFromMessage(data);
                break;
            default:
                terminal.println("Unknown packet type recieved");
        }
    }

    public synchronized void sendMessageOn(byte[] data, int src) {
        byte[] message = new byte[data[LENGTH_POS]];
        System.arraycopy(data, HEADER_LENGTH, message, 0, data[LENGTH_POS]);
        String messageString = new String(message);
        System.out.println(messageString);
        String[] split = messageString.split("\\|");
        while (flowTable.getNextHop(Integer.parseInt(split[1])) == -1) {
            try {
                sendFeatureRequest(split[1], src);
                terminal.println("No route saved, asking for a new one");
                wait(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        InetSocketAddress nextHop = new InetSocketAddress("localhost", flowTable.getNextHop(Integer.parseInt(split[1])));
        sendMessage(nextHop, messageString, TYPE_DATA);

    }

    public void sendFeatureRequest(String dest, int src) throws Exception {
        sendMessage(controlAddress, dest+"|"+src, TYPE_FEATURE_REQUEST);
    }

    public void addRouteFromMessage(byte[] data) {
        byte[] message = new byte[data[LENGTH_POS]];
        System.arraycopy(data, HEADER_LENGTH, message, 0, data[LENGTH_POS]);
        String messageString = new String(message);
        String[] split = messageString.split(",");
        int dest = Integer.parseInt(split[0]);
        int out = Integer.parseInt(split[1]);
        int in = Integer.parseInt(split[2]);
        flowTable.addFlow(dest, out, in);
    }

    @Override
    public synchronized void run() {
        try {
            while (true) {
                wait(500);
                if (currentData != null) {
                    sendMessageOn(currentData.getData(), currentData.getPort());
                    currentData = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        execute();
    }

    public static void execute() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        executorService.submit(Router::createAndRunRouter1);
        executorService.submit(Router::createAndRunRouter2);
        executorService.submit(Router::createAndRunRouter3);
        executorService.submit(Router::createAndRunRouter4);

        executorService.shutdown();
    }

    public static void createAndRunRouter1() {
        Terminal terminal = new Terminal("Router 1");
        Router router = new Router(40012, terminal);
        router.run();
    }

    public static void createAndRunRouter2() {
        Terminal terminal = new Terminal("Router 2");
        Router router = new Router(40013, terminal);
        router.run();
    }

    public static void createAndRunRouter3() {
        Terminal terminal = new Terminal("Router 3");
        Router router = new Router(40014, terminal);
        router.run();
    }

    public static void createAndRunRouter4() {
        Terminal terminal = new Terminal("Router 4");
        Router router = new Router(40015, terminal);
        router.run();
    }
}
