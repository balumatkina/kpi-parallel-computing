import java.util.Random;

public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public static final int ARRAY_SIZE = 1000000;
    public static final int MAX_ARRAY_ELEMENT_VALUE = 8000;

    public static void main(String[] args) {
        Random random = new Random();
        System.out.println(ANSI_PURPLE +
                "TASK: Find the number of odd elements in an array and the largest odd number"
                + ANSI_RESET);
        System.out.println("Array size: " + ARRAY_SIZE);
        System.out.println();

        long startTime = System.nanoTime();
        int[] array = new int[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = random.nextInt(MAX_ARRAY_ELEMENT_VALUE + 1);
        }

        OddNumberCounter counter = new OddNumberCounter(array);
        counter.countOdds();

        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        System.out.println("Amount of used threads: " + counter.nThreads);
        System.out.println();
        System.out.println("Task completed in: " + duration +
                " nanoseconds (" + duration / 1000000 + " milliseconds)");
        System.out.println("Waiting lock time: " + counter.mutexWaitTime+ " nanoseconds ("
                + counter.mutexWaitTime / 1000000 + " milliseconds)");
        System.out.println();
        System.out.println("Number of odd elements: " + counter.getOddCount());
        System.out.println("Largest odd number: " + counter.getMaxOdd());
    }
}