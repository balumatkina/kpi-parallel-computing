import java.util.Random;

public class TaskGenerator {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    private final CustomThreadPool threadPool;
    private final Random random;
    private int taskIdCounter;

    public TaskGenerator(CustomThreadPool threadPool) {
        this.threadPool = threadPool;
        this.random = new Random();
        this.taskIdCounter = 1;
    }

    public void addTasks(int numTasks) {
        for (int i = 0; i < numTasks; i++) {
            final int taskId = taskIdCounter++;
            Runnable task = () -> {
                int executeTime = random.nextInt(6000) + 6000;
                System.out.println("TASK START AND DATA: Task " + taskId + " has execution time of " + executeTime + "ms");

                long startTime = System.currentTimeMillis();
                try {
                    Thread.sleep(executeTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // set the interrupted status
                    System.out.println(ANSI_RED + "WARNING: Task " + taskId + " was interrupted" + ANSI_RESET);
                    return;
                } catch (Throwable t) {
                    System.out.println(ANSI_RED + "WARNING: Task " + taskId + " threw an unexpected error: " + t.getMessage() + ANSI_RESET);
                return;
            }
                long endTime = System.currentTimeMillis();

                System.out.println("TASK DONE: Task " + taskId + " finished in " + (endTime - startTime) + "ms");
            };
            try {
                threadPool.execute(task);
                System.out.println("THREAD: Task " + taskId + " added to thread pool");
            } catch (Throwable t) {
                System.err.println(ANSI_RED + "THREAD: Task " + taskId + " threw an unexpected error when added to thread pool: " + t.getMessage() + ANSI_RESET);
            }
        }
    }
}
