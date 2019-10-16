import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Scanner;

public class Worker extends Node{
	private final byte TYPE_DATA = 0;
	private final byte TYPE_ACK = 1;
	private final byte TYPE_CONNECTION = 2;
	private final byte TYPE_CONNECTION_ACK = 3;
	private final byte TYPE_ORDER_ACCERPTED = 4;
	private final byte TYPE_ORDER_DECLINED = 5;
	private final int TYPE_POS = 0;
	
	private final byte FRAME_1 = 0;
	private final byte FRAME_2 = 1;
	private final byte FRAME_3 = 2;
	private final byte FRAME_4 = 3;
	private final int FRAME_POS = 1;

	private final int NODE_POS = 2;
	
	private final int LENGTH_POS = 3;
	private final int HEADER_LENGTH = 4;
	
	private final int BROKER_SOCKET = 45000;
	private final String BROKER_NODE = "localhost";
	
	InetSocketAddress brokerAddress;

	private boolean connected = false;
	private boolean ackRecieved = false;
	
	
	
	public Worker(int socket) {
		try {
		brokerAddress = new InetSocketAddress(BROKER_NODE, BROKER_SOCKET);
		this.socket = new DatagramSocket(socket);
		listener.go();
		connectToServer();
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
		String toSend = "Hello world";
		byte[] message = toSend.getBytes();
		data = new byte[message.length + HEADER_LENGTH];
		data[TYPE_POS] = TYPE_DATA;
		data[FRAME_POS] = 0;
		data[NODE_POS] = WORKER_TYPE;
		data[LENGTH_POS] = (byte) message.length;
		System.arraycopy(message, 0, data, HEADER_LENGTH, message.length);
		packet = new DatagramPacket(data, data.length);
		packet.setSocketAddress(brokerAddress);
		socket.send(packet);
		this.wait();
		
	}

	@Override
	public synchronized void sendAck(SocketAddress returnAddress) throws Exception {
		byte[] data = new byte[HEADER_LENGTH];
		DatagramPacket packet = null;
		data[TYPE_POS] = TYPE_ACK;
		data[FRAME_POS] = 0;
		data[NODE_POS] = WORKER_TYPE;
		packet = new DatagramPacket(data, data.length);
		packet.setSocketAddress(returnAddress);
		socket.send(packet);
		System.out.println("Ack sent");
	}
	@Override
	public synchronized void onReceipt(DatagramPacket packet) {
		try {
			String content;
			byte[] data = packet.getData();


			switch (data[TYPE_POS]) {
				case TYPE_DATA:
					byte[] byteContent = new byte[data.length - HEADER_LENGTH];
					System.arraycopy(data, HEADER_LENGTH, byteContent, 0, data.length-HEADER_LENGTH);
					content = new String(byteContent);
					System.out.println("Command said: " + content);
					sendAck(packet.getSocketAddress());
					acceptOrDeclineOrder();
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

	public void acceptOrDeclineOrder(){

		boolean correctInput = false;
		boolean accepted = true;
		while (!correctInput) {
			System.out.println("Would you like to accept this order? [Y:N]");
			Scanner sc = new Scanner(System.in);
			if (sc.hasNext("Y")) {
				correctInput = true;
				accepted = true;
			} else if (sc.hasNext("N")) {
				accepted = false;
				correctInput = true;
			}
			else {
				System.out.println("Incorrect input, please try again");
				continue;
			}
			try {
				sendOrderReply(accepted);
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	public void sendOrderReply(boolean accepted) throws IOException {
		byte[] data = new byte[HEADER_LENGTH];
		DatagramPacket packet = null;
		if (accepted){
			data[TYPE_POS] = TYPE_ORDER_ACCERPTED;
		}
		else{
			data[TYPE_POS] = TYPE_ORDER_DECLINED;
		}
		data[FRAME_POS] = 0;
		data[NODE_POS] = WORKER_TYPE;
		packet = new DatagramPacket(data, data.length);
		packet.setSocketAddress(brokerAddress);
		socket.send(packet);
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
			data[NODE_POS] = WORKER_TYPE;
			packet = new DatagramPacket(data, data.length);
			packet.setSocketAddress(brokerAddress);
			socket.send(packet);
			System.out.println("No connection yet, trying again...");
			this.wait(2000);

		}
	}

	public static void main(String[] args) {
		try {
			Worker worker1 = new Worker(50005);
			worker1.run();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

}
