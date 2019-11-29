import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;


public abstract class Node {
    static final int CONTROL_ADDRESS = 40000;
    private static final int PACKET_SIZE = 1024;

    final byte TYPE_DATA = 0;
    final byte TYPE_ACK = 1;
    final byte TYPE_HELLO = 2;
    final byte TYPE_FEATURE_REQUEST = 3;
    final byte TYPE_FEATURE_RESPONSE = 4;
    final byte TYPE_USER_HELLO = 5;
    final int TYPE_POS = 0;

    final int LENGTH_POS = 1;
    final int HEADER_LENGTH = 2;

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
        if (type == TYPE_DATA || type == TYPE_FEATURE_REQUEST || type == TYPE_FEATURE_RESPONSE) {
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

    public abstract void sendMessage(SocketAddress address, String message, byte type) throws Exception;

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
