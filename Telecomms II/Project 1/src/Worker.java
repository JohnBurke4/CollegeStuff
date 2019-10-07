import java.net.DatagramPacket;

public class Worker extends Node{
	private final byte TYPE_DATA = 0;
	private final byte TYPE_ACK = 1;
	private final int TYPE_POS = 0;
	
	private final int FRAME_POS = 1;
	
	private final byte WORKER_TYPE = 0;
	private final byte C_AND_C_TYPE = 1;
	private final int NODE_POS = 2;
	
	private final int DATA_POS = 3;
	private final int HEADER_LENGTH = 3;
	
	private int socket;
	
	public Worker(int socket) {
		super();
		this.socket = socket;
	}
	
	

	@Override
	public void sendMessage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceipt(DatagramPacket packet) {
		// TODO Auto-generated method stub
		
	}

}
