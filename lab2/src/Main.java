public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public static final int NUM_THREADS_IN_THREAD_POOL = 4;
    public static final int NUM_TOTAL_TASKS = 30;
    public static final int THREAD_SLEEP_TIME = 45000;
    public static final int NUM_PROGRAM_ITERATION = 3;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < NUM_PROGRAM_ITERATION; i++) {

            System.out.println();
            System.out.println(ANSI_PURPLE + "Iteration #" + (i + 1) + ANSI_RESET);
            System.out.println();

            CustomThreadPool threadPool = new CustomThreadPool(NUM_THREADS_IN_THREAD_POOL);
            TaskGenerator taskGenerator = new TaskGenerator(threadPool);
            taskGenerator.addTasks(NUM_TOTAL_TASKS);

            try {
                long startTime = System.currentTimeMillis();
                Thread.sleep(THREAD_SLEEP_TIME);
                //45 seconds wait before stopping the thread pool
                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;

                System.out.println();
                System.out.println(ANSI_PURPLE + "-".repeat(30));
                System.out.println("Thread pool finished in " + elapsedTime + "ms" + ANSI_RESET);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(ANSI_RED + "WARNING: Interrupted Thread." + ANSI_RESET);
            }

            if (Thread.interrupted()) {
                threadPool.stop();
                throw new InterruptedException();
            }

            threadPool.printStatistics();
            threadPool.stop();
        }
    }
}
