import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.*;

public class MedianCalculator {
    private final Vector<Float> vector;
    private final int nCores;

    public MedianCalculator(Vector<Float> vector, int nCores) {
        this.vector = vector;
        this.nCores = nCores;
    }

    public float calculateMedianParallel() {
        int vectorSize = vector.size();
        int chunkSize = vectorSize / nCores;
        int i = 0;

        //chunks aka parts of vector, minecraft hello
        Vector<List<Float>> chunks = new Vector<>();
        while (i < vectorSize) {
            int j = i + chunkSize;
            if (j > vectorSize) {
                j = vectorSize;
            }
            chunks.add(vector.subList(i, j));
            //take parts from sublist, copy to chunks
            i = j;
        }

        ExecutorService executor = Executors.newFixedThreadPool(nCores);
        Vector<Future<Float>> futures = new Vector<>();
        for (final List<Float> chunk : chunks) {
            Callable<Float> task = () -> {
                float[] chunkArray = new float[chunk.size()];
                for (int i1 = 0; i1 < chunk.size(); i1++) {
                    chunkArray[i1] = chunk.get(i1);
                }
                Arrays.sort(chunkArray);
                int middle = chunkArray.length / 2;
                if (chunkArray.length % 2 == 0) {
                    return (chunkArray[middle - 1] + chunkArray[middle]) / 2;
                } else {
                    return chunkArray[middle];
                }
            };
            futures.add(executor.submit(task));
        }

        executor.shutdown();

        //future.get() instead of join() + all the exceptions from future and callable
        Vector<Float> medians = new Vector<>();
        for (Future<Float> future : futures) {
            try {
                medians.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        //searching for median of medians with parallel reduction algorithm
        while (medians.size() > 1) {
            int n = medians.size();
            int m = (n + 1) / 2;
            for (int j = 0; j < m; j++) {
                medians.set(j, medians.get(j * 2));
            }
            medians.setSize(m);
        }
        return medians.get(0);
    }
}
