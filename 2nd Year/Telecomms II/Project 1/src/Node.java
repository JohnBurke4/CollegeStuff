import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;


public abstract class Node {
	static final int PACKET_SIZE = 65536;
	public final byte WORKER_TYPE = 0;
	public final byte C_AND_C_TYPE = 1;
	public final byte BROKER_TYPE = 2;


	public final byte TYPE_DATA = 0;
	public final byte TYPE_ACK = 1;
	public final byte TYPE_CONNECTION = 2;
	public final byte TYPE_CONNECTION_ACK = 3;
	public final int TYPE_POS = 0;

	public final byte FRAME_1 = 0;
	public final byte FRAME_2 = 1;
	public final byte FRAME_3 = 2;
	public final byte FRAME_4 = 3;
	public final int FRAME_POS = 1;
	
	DatagramSocket socket;
	Listener listener;
	CountDownLatch latch;
	
	Node() {
		latch = new CountDownLatch(1);
		listener = new Listener();
		listener.setDaemon(true);
		listener.start();
	}
	
	public abstract void sendMessage() throws Exception;

	public abstract void sendAck(SocketAddress returnAddress) throws Exception;
	
	public abstract void onReceipt(DatagramPacket packet);
	
	public abstract void run();

	public abstract void connectToServer() throws Exception;
	
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
