import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import sun.security.provider.JavaKeyStore.CaseExactJKS;

public class Broker extends Node{
	private final byte TYPE_DATA = 0;
	private final byte TYPE_ACK = 1;
	private final int TYPE_POS = 0;
	
	private final byte FRAME_1 = 0;
	private final byte FRAME_2 = 1;
	private final byte FRAME_3 = 2;
	private final byte FRAME_4 = 3;
	private final int FRAME_POS = 1;
	
	private final byte WORKER_TYPE = 0;
	private final byte C_AND_C_TYPE = 1;
	private final int NODE_POS = 2;
	
	private final int LENGTH_POS = 3;
	private final int HEADER_LENGTH = 3;
	
	private final int BROKER_SOCKET = 45000;
	private final String BROKER_NODE = "localhost";
	
	InetSocketAddress brokerAddress;
	
	
	
	public Broker() {
		try {
		brokerAddress = new InetSocketAddress(BROKER_NODE, BROKER_SOCKET);
		this.socket = new DatagramSocket(BROKER_SOCKET);
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
		String toSend = "test";
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
	public synchronized void onReceipt(DatagramPacket packet) {
		// TODO Auto-generated method stub
		try {
			String content;
			byte[] data = packet.getData();
			
			switch (data[TYPE_POS]) {
			case TYPE_DATA:
				break;
			case TYPE_ACK:
				break;
			default:
				System.out.println("Error, wrong data type");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void run() {
		while(true) {
			
		}
	}

}
