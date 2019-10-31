import java.util.ArrayList;
import java.util.Arrays;

public class SerialTest {

    public static final int HEADER_LENGTH = 5;

    public static ArrayList<byte[]> packetParts = new ArrayList<byte[]>();

    public static void main(String[] args){
        BrokerCommand command = new BrokerCommand("Do stuff", null, 1);
        byte[] serial = BrokerCommand.getSerialized(command);
        byte[] message;

        System.out.println(serial.length);

        int index = 0;
        while (index + 1024 - HEADER_LENGTH < serial.length){
            message = Arrays.copyOfRange(serial, index, index + 1024 - HEADER_LENGTH);
            index+= 1024 - HEADER_LENGTH;
            packetParts.add(message);
        }
        message = Arrays.copyOfRange(serial, index, serial.length);
        packetParts.add(message);

        int size = 0;
        for (byte[] packet : packetParts){
            size += packet.length;
        }

        serial = new byte[(1024 - HEADER_LENGTH) * packetParts.size()];

        index = 0;
        for (byte[] packet: packetParts){
            System.arraycopy(packet, 0, serial, index, packet.length);
            index += packet.length;
        }


        System.out.println(serial.length);

        command = BrokerCommand.makeFromSerialized(serial);
        System.out.println(command.getCommand());

        System.out.println((byte) 256);
    }
}
