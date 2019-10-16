import java.io.*;
import java.net.SocketAddress;
import java.util.ArrayList;

public class BrokerCommand implements Serializable{
    private ArrayList<SocketAddress> workerAddresses = new ArrayList<SocketAddress>();
    private SocketAddress sender;
    private String command;
    private boolean isComplete;
    private int numberOfWorkers;

    public BrokerCommand(String command, SocketAddress sender, int numberOfWorkers){
        this.command = command;
        this.sender = sender;
        this.numberOfWorkers = numberOfWorkers;
    }

    public void setCommand(String command){
        this.command = command;
    }

    public String getCommand(){
        return command;
    }

    public void setComplete(boolean complete){
        this.isComplete = complete;
    }

    public boolean getComplete(){
        return isComplete;
    }

    public void addWorker(SocketAddress workerAddress) {
        workerAddresses.add(workerAddress);
    }

    public boolean isWorker(SocketAddress workerAddress){
        return workerAddresses.contains(workerAddress);
    }

    public void setSender(SocketAddress sender){
        this.sender = sender;
    }

    public SocketAddress getSender(){
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

        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (Exception e) {

            }
        }
        return serialized;
    }

    public static BrokerCommand makeFromSerialized(byte[] serial){
        ByteArrayInputStream bis = new ByteArrayInputStream(serial);
        ObjectInput in = null;
        BrokerCommand command = null;
        try {
            in = new ObjectInputStream(bis);
            command = (BrokerCommand) in.readObject();
        } catch (Exception e){
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

    public class WorkerCommand implements Serializable{
        private boolean complete;
        private String command;

        public WorkerCommand(String command){
            this.command = command;
        }

        public void setComplete(boolean complete){
            this.complete = complete;
        }

        public boolean getComplete(){
            return complete;
        }

        public String getCommand(){
            return command;
        }



    }

    public static byte[] getWorkerSerializable(WorkerCommand command){
        byte[] serialized = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(command);
            out.flush();
            serialized = bos.toByteArray();

        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (Exception e) {

            }
        }
        return serialized;
    }

    public static WorkerCommand makeWorkerFromSerialized(byte[] serial){
        ByteArrayInputStream bis = new ByteArrayInputStream(serial);
        ObjectInput in = null;
        WorkerCommand command = null;
        try {
            in = new ObjectInputStream(bis);
            command = (WorkerCommand) in.readObject();
        } catch (Exception e){
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
