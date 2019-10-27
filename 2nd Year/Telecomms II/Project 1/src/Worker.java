import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Scanner;

public class Worker extends Node{

	private final int BROKER_SOCKET = 45000;
	private final String BROKER_NODE = "localhost";
	
	InetSocketAddress brokerAddress;

	private boolean connected = false;

	private BrokerCommand.WorkerCommand currentCommand = null;
	
	
	
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
		
	}

	@Override
	public synchronized void sendAck(SocketAddress returnAddress) throws Exception {
		socket.send(makePacket(returnAddress, null, TYPE_ACK, FRAME_1, WORKER_TYPE));
		System.out.println("Ack sent");
	}

	@Override
	public synchronized void onReceipt(DatagramPacket packet) {
		try {
			byte[] data = packet.getData();
			switch (data[TYPE_POS]) {
				case TYPE_DATA:
					byte[] byteContent = new byte[data.length - HEADER_LENGTH];
					System.arraycopy(data, HEADER_LENGTH, byteContent, 0, data.length-HEADER_LENGTH);
					BrokerCommand.WorkerCommand command = BrokerCommand.makeWorkerFromSerialized(byteContent);
					System.out.println("Command said: " + command.getCommand());
					currentCommand = command;
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
				if (accepted){
					sayIfFinished();
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	public synchronized void sendOrderReply(boolean accepted) throws IOException {
		currentCommand.setAccepted(accepted);
		byte[] command = BrokerCommand.getWorkerSerializable(currentCommand);
		if (!currentCommand.getAccepted()){
			System.out.println("Sending refusal");
		}
		socket.send(makePacket(brokerAddress, command, TYPE_DATA, FRAME_1, WORKER_TYPE));
	}

	public void sayIfFinished(){

		boolean correctInput = false;
		while (!correctInput) {
			System.out.println("Press [F] if you have finished the order, anything else to see the order again");
			Scanner sc = new Scanner(System.in);
			if (sc.hasNext("F")) {
				correctInput = true;
				currentCommand.setComplete(true);
			} else {
				System.out.println("Current Order: " + currentCommand.getCommand());
				continue;
			}
			try {
				sendOrderReply(true);
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void run() {
		while(true) {
			try {
			//sendMessage();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public synchronized void connectToServer() throws Exception {
		while (!connected){
			socket.send(makePacket(brokerAddress, null, TYPE_CONNECTION, FRAME_1, WORKER_TYPE));
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
			e.printStackTrace();
		}
	}

}
