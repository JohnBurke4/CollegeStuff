import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Control extends Node {
    Terminal terminal;

    public Control(int address, Terminal terminal) {
        try {
            this.terminal = terminal;
            this.socket = new DatagramSocket(address);
            this.listener.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(SocketAddress address, String message) throws Exception {

    }

    @Override
    public void sendAck(SocketAddress returnAddress) throws Exception {

    }

    @Override
    public void onReceipt(DatagramPacket packet) {
        try {
            byte[] data = packet.getData();
            switch (data[TYPE_POS]) {
                case TYPE_CONNECTION:
                    terminal.println("Router Connected");
                    socket.send(makePacket(packet.getSocketAddress(), null, TYPE_CONNECTION_ACK));
                    break;
                default:
                    terminal.println("Unknown packet type recieved");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }

    @Override
    public void connectToControl() throws Exception {

    }

    public static void main(String[] args){
        Terminal terminal = new Terminal("Control");
        Control control = new Control(CONTROL_ADDRESS, terminal);
    }
}
