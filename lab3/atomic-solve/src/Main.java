import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public static final int ARRAY_SIZE = 1000000;
    public static final int MAX_ARRAY_ELEMENT_VALUE = 8000;

    public static void main(String[] args) throws InterruptedException {
        System.out.println(ANSI_PURPLE +
                "TASK: Find the number of odd elements in an array and the largest odd number"
                + ANSI_RESET);
        System.out.println("Array size: " + ARRAY_SIZE);

        Random random = new Random();

        int numThreads = Runtime.getRuntime().availableProcessors(); // number of threads to use
        int totalOddCount = 0;

        AtomicIntegerArray oddNumbCount = new AtomicIntegerArray(numThreads);
        AtomicInteger maxOddElement = new AtomicInteger(0);

        long startTime = System.nanoTime();
        ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
        int[] array = new int[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = random.nextInt(MAX_ARRAY_ELEMENT_VALUE + 1);
        }

        for (int i = 0; i < numThreads; i++) {
            final int threadID = i;
            threadPool.execute(() -> {
//                int localMax = 0;
                for (int j = threadID; j < array.length; j += numThreads) {
                    int num = array[j];
                    if (num % 2 != 0) {
                        // increment count for this thread
                        oddNumbCount.getAndIncrement(threadID);
                        // update max if necessary
                        while (true) {
                            int currMax = maxOddElement.get();
                            if (num > currMax) {
                                if (maxOddElement.compareAndSet(currMax, num)) {
//                                    localMax = num;
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
//                System.out.println("Thread " + threadID + " finished with max odd number: " + localMax);
            });
        }

        threadPool.shutdown();
        threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        for (int i = 0; i < numThreads; i++) {
            totalOddCount += oddNumbCount.get(i);
        }

        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        System.out.println("Threads amount: " + numThreads);
        System.out.println();
        System.out.println("Task completed in: " + duration +
                " nanoseconds (" + duration / 1000000 + " milliseconds)");
        System.out.println("Number of odd elements: " + totalOddCount);
        System.out.println("Largest odd number: " + maxOddElement.get());
    }
}
