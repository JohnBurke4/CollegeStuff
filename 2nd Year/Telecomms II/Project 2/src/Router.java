import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Router extends Node {

    InetSocketAddress controlAddress;
    boolean connected = false;
    Terminal terminal;

    public Router(int address, Terminal terminal){
        try{
            this.terminal = terminal;
            this.socket = new DatagramSocket(address);
            this.listener.go();
            controlAddress = new InetSocketAddress("localhost", CONTROL_ADDRESS);
            connectToControl();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(SocketAddress address, String message) throws Exception {
        byte[] serial = message.getBytes();
        socket.send(makePacket(address, serial, TYPE_DATA));
    }

    @Override
    public void sendAck(SocketAddress returnAddress) throws Exception {
        socket.send(makePacket(returnAddress, null, TYPE_ACK));
    }

    @Override
    public void onReceipt(DatagramPacket packet) {
        byte[] data = packet.getData();
        switch (data[TYPE_POS]){
            case TYPE_CONNECTION_ACK:
                terminal.println("Connected to Control");
                connected = true;
                break;
            default:
                terminal.println("Unknown packet type recieved");
        }
    }

    @Override
    public void run() {

    }

    @Override
    public synchronized void connectToControl() throws Exception {
        socket.send(makePacket(controlAddress, null, TYPE_CONNECTION));
        wait(100);
        while (!connected){
            terminal.println("No connection, trying again...");
            socket.send(makePacket(controlAddress, null, TYPE_CONNECTION));
            wait(3000);
        }
    }

    public static void main(String[] args){
        Terminal terminal = new Terminal("Router 1");
        Router router1 = new Router(45000, terminal);
        terminal = new Terminal("Router 2");
        Router router2 = new Router(45001, terminal);
        terminal = new Terminal("Router 3");
        Router router3 = new Router(45002, terminal);
    }
}
