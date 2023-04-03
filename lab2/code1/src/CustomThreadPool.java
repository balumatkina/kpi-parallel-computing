public class CustomThreadPool {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    private final CustomBlockingQueue<Runnable> taskQueue;
    private final Thread[] threads; //working threads array
    // -> workerthread
    private volatile boolean running;

    //statistics
    private volatile int numTasksExecuted;
    private volatile long totalExecutionTime;

    public CustomThreadPool(int numWorkers) {
        this.taskQueue = new CustomBlockingQueue<>();
        this.threads = new Thread[numWorkers];
        this.running = true;
        this.numTasksExecuted = 0;
        this.totalExecutionTime = 0;
        for (int i = 0; i < numWorkers; i++) {
            threads[i] = new WorkerThread();
            threads[i].start();
        }
    }

    public void execute(Runnable task) throws InterruptedException {
        taskQueue.put(task);
    }

    public void taskFinished() {
        synchronized (this) {
            numTasksExecuted++;
        }
    }

    public int getNumTasksExecuted() {
        synchronized (this) {
            return numTasksExecuted;
        }
    }

    public long getTotalExecutionTime() {
        synchronized (this) {
            return totalExecutionTime;
        }
    }

    public void stop() { //to stop the thread pool
        running = false;
        for (Thread thread : threads) {
            thread.interrupt();
        }
        taskQueue.clear();
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        synchronized (this) {
            numTasksExecuted = 0;
            totalExecutionTime = 0;
        }
    }


    private class WorkerThread extends Thread {
        @Override
        public void run() {
            while (running && !isInterrupted()) {
                try {
                    Runnable task = taskQueue.take();
                    System.out.println("Task is given to: " + Thread.currentThread().getName());
                    long startTime = System.currentTimeMillis();
                    task.run();
                    long endTime = System.currentTimeMillis();
                    synchronized (CustomThreadPool.this) {
                        totalExecutionTime += endTime - startTime;
                    }
                    taskFinished();
                } catch (InterruptedException e) {
                    interrupt(); // re-interrupting the thread
                }
            }
        }
    }

    public void printStatistics() {
        System.out.println(ANSI_PURPLE + "-".repeat(30));
        System.out.println("Number of tasks executed: " + getNumTasksExecuted());
        System.out.println("Total execution time: " + getTotalExecutionTime() + "ms");
        System.out.println("-".repeat(30) + ANSI_RESET);
        System.out.println();
    }
}
