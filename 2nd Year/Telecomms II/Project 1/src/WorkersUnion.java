import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkersUnion {
    public void createAndStartWorker1() {
        Terminal terminal = new Terminal("Client 1");
        Worker worker1 = new Worker(terminal, 50005);
    }

    public void createAndStartWorker2() {
        Terminal terminal = new Terminal("Client 2");
        Worker worker2 = new Worker(terminal, 50006);
    }

    public void createAndStartWorker3() {
        Terminal terminal = new Terminal("Client 3");
        Worker worker2 = new Worker(terminal, 50007);
    }

    public void execute() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        executorService.submit(this::createAndStartWorker1);
        executorService.submit(this::createAndStartWorker2);
        executorService.submit(this::createAndStartWorker3);

        executorService.shutdown();
    }

    public static void main(String[] args) {
        new WorkersUnion().execute();
    }
}
