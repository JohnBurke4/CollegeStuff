import java.io.*;
import java.net.SocketAddress;
import java.util.ArrayList;

public class BrokerCommand implements Serializable {
    private SocketAddress sender;
    private String command;
    private boolean isComplete;
    private int numberOfWorkers;
    private int numberAccepted = 0;
    private int numberOrdersSend = 0;
    private int numberComplete = 0;
    private byte[] padding;

    public BrokerCommand(String command, SocketAddress sender, int numberOfWorkers) {
        this.command = command;
        this.sender = sender;
        this.numberOfWorkers = numberOfWorkers;
    }

    public void setPadding(int size) {
        padding = new byte[size];
    }


    public int getWorkersNeeded() {
        return (numberOrdersSend - numberComplete);
    }

    public void incrementNumberOrdersSent() {
        numberOrdersSend++;
    }

    public void decrementNumberOrdersSent() {
        numberOrdersSend--;
    }

    public boolean checkIfEnoughOrdersSent() {
        return numberOrdersSend >= numberOfWorkers;
    }

    public int getNumberOfOrdersSent() {
        return numberOrdersSend;
    }

    public void incrememtNumberAccepted() {
        numberAccepted++;
    }

    public void incrementNumberComplete() {
        numberComplete++;
    }

    public int getNumberNumberAccepted() {
        return numberAccepted;
    }

    public int getNumberComplete() {
        return numberComplete;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setComplete() {
        this.isComplete = (numberComplete >= numberOfWorkers);
    }

    public boolean getComplete() {
        return isComplete;
    }

    public void setSender(SocketAddress sender) {
        this.sender = sender;
    }

    public SocketAddress getSender() {
        return sender;
    }

    public static byte[] getSerialized(BrokerCommand thisCommand) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] serialized = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(thisCommand);
            out.flush();
            serialized = bos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (Exception e) {

            }
        }
        return serialized;
    }

    public static BrokerCommand makeFromSerialized(byte[] serial) {
        ByteArrayInputStream bis = new ByteArrayInputStream(serial);
        ObjectInput in = null;
        BrokerCommand command = null;
        try {
            in = new ObjectInputStream(bis);
            command = (BrokerCommand) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return command;
    }


}
