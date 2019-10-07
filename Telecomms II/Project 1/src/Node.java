import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;


public abstract class Node {
	static final int PACKET_SIZE = 65536;
	
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
	
	public abstract void onReceipt(DatagramPacket packet);
	
	public abstract void run();
	
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
