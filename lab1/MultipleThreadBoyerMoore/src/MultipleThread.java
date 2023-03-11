import java.util.*;

public class MultipleThread {
    private static final int VECTOR_SIZE = 1000000;
    private static final int THREADS_AMOUNT = 8;
    private static final float MIN_VALUE = -1000;
    private static final float MAX_VALUE = 1000;

    public static void main(String[] args) {
        Vector<Float> vector = generateRandomVector(VECTOR_SIZE, MIN_VALUE, MAX_VALUE);

        ModeCalculator modeCalculator = new ModeCalculator(vector);
        long startTime = System.nanoTime();
        float mode = modeCalculator.calculateMode();
        long modeTime = System.nanoTime() - startTime;

        MedianCalculator medianCalculator = new MedianCalculator(vector, THREADS_AMOUNT);
        startTime = System.nanoTime();
        float median = medianCalculator.calculateMedianParallel();
        long medianTime = System.nanoTime() - startTime;

        System.out.println("MULTIPLE THREAD PROGRAM");
        System.out.println("Amount of threads: " + THREADS_AMOUNT);
        System.out.println("Amount of elements in the vector: " + VECTOR_SIZE + "\n");

        System.out.println("MODE: " + mode);
        System.out.println("Execution time (mode): " + modeTime + " ns (" + modeTime / 1e9 + " sec)\n");

        System.out.println("MEDIAN: " + median);
        System.out.println("Execution time (median): " + medianTime + " ns (" + medianTime / 1e9 + " sec)\n");

        long totalTime = modeTime + medianTime;
        System.out.println("Total time for two functions: " + totalTime + " ns (" + totalTime/1e9 + " sec)");
    }

    private static Vector<Float> generateRandomVector(int size, float min, float max) {
        Random random = new Random();
        Vector<Float> vector = new Vector<>(size);
        for (int i = 0; i < size; i++) {
            vector.add(min + random.nextFloat() * (max - min));
        }
        return vector;
    }
}

