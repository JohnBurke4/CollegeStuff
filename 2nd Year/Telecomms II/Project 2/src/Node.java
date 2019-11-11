import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;


public abstract class Node {
    static final int CONTROL_ADDRESS = 40000;
    static final int PACKET_SIZE = 1024;

    public final byte TYPE_DATA = 0;
    public final byte TYPE_ACK = 1;
    public final byte TYPE_CONNECTION = 2;
    public final byte TYPE_CONNECTION_ACK = 3;
    public final int TYPE_POS = 0;

    public final int LENGTH_POS = 1;
    public final int HEADER_LENGTH = 2;

    DatagramSocket socket;
    Listener listener;
    CountDownLatch latch;

    Node() {
        latch = new CountDownLatch(1);
        listener = new Listener();
        listener.setDaemon(true);
        listener.start();
    }

    public DatagramPacket makePacket(SocketAddress address, byte[] message, byte type) {
        if (type == TYPE_DATA) {
            byte[] data = new byte[HEADER_LENGTH + message.length];
            data[TYPE_POS] = type;
            data[LENGTH_POS] = (byte) message.length;
            System.arraycopy(message, 0, data, HEADER_LENGTH, message.length);
            DatagramPacket packet = new DatagramPacket(data, data.length);
            packet.setSocketAddress(address);
            return packet;
        } else {
            byte[] header = new byte[HEADER_LENGTH];
            header[TYPE_POS] = type;
            header[LENGTH_POS] = 0;
            DatagramPacket packet = new DatagramPacket(header, header.length);
            packet.setSocketAddress(address);
            return packet;
        }
    }

    public abstract void sendMessage(SocketAddress address, String message) throws Exception;

    public abstract void sendAck(SocketAddress returnAddress) throws Exception;

    public abstract void onReceipt(DatagramPacket packet);

    public abstract void run();

    public abstract void connectToControl() throws Exception;

    class Listener extends Thread {
        public void go() {
            latch.countDown();
        }

        public void run() {
            try {
                latch.await();


                while (true) {
                    DatagramPacket packet = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
                    socket.receive(packet);

                    onReceipt(packet);
                }
            } catch (Exception e) {
                if (!(e instanceof SocketException))
                    e.printStackTrace();
            }
        }
    }

}
