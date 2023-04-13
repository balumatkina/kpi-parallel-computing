import java.util.concurrent.*;

public class OddNumberCounter {
    private int[] array;
    private int oddCount;
    private int maxOdd;
    private Semaphore mutex;
    public long mutexWaitTime;
    public int nThreads;

    public OddNumberCounter(int[] array) {
        this.array = array;
        this.oddCount = 0;
        this.maxOdd = 0;
        this.mutex = new Semaphore(1);
        this.nThreads = Runtime.getRuntime().availableProcessors();
    }

    public int getOddCount() {
        return oddCount;
    }

    public int getMaxOdd() {
        return maxOdd;
    }

    public void countOdds() {
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);

        int chunkSize = array.length / nThreads;
        for (int i = 0; i < nThreads; i++) {
            int startIndex = i * chunkSize;
            int endIndex = (i == nThreads - 1) ? array.length : (i + 1) * chunkSize;
            executor.submit(new OddNumberCounterTask(startIndex, endIndex));
        }

        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    private class OddNumberCounterTask implements Runnable {
        private int startIndex;
        private int endIndex;

        public OddNumberCounterTask(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        public void run() {
            for (int i = startIndex; i < endIndex; i++) {
                if (array[i] % 2 == 1) {
                    long startWaitTime = System.nanoTime();
                    try {
                        mutex.acquire();
                        long endWaitTime = System.nanoTime();
                        mutexWaitTime += endWaitTime - startWaitTime;
                        oddCount++;
                        if (array[i] > maxOdd) {
                            maxOdd = array[i];
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        mutex.release();
                    }
                }
            }
        }
    }
}
