import java.io.*;

public class WorkerCommand implements Serializable {
    private boolean complete;
    private String command;
    private boolean accepted;

    public WorkerCommand(String command) {
        this.command = command;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean getComplete() {
        return complete;
    }

    public String getCommand() {
        return command;
    }

    public boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public static WorkerCommand makeWorkerFromSerialized(byte[] serial) {
        ByteArrayInputStream bis = new ByteArrayInputStream(serial);
        ObjectInput in = null;
        WorkerCommand command = null;
        try {
            in = new ObjectInputStream(bis);
            command = (WorkerCommand) in.readObject();
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

    public static byte[] getWorkerSerializable(WorkerCommand command) {
        byte[] serialized = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(command);
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
}