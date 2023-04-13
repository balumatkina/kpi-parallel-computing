import java.util.Random;

public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public static final int ARRAY_SIZE = 1000000;
    public static final int MAX_ARRAY_ELEMENT_VALUE = 8000;

    public static void main(String[] args) {
        System.out.println(ANSI_PURPLE +
                "TASK: Find the number of odd elements in an array and the largest odd number"
                + ANSI_RESET);
        System.out.println("Array size: " + ARRAY_SIZE);

        Random random = new Random();
        long startTime = System.nanoTime();
        int[] array = new int[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = random.nextInt(MAX_ARRAY_ELEMENT_VALUE + 1);
        }

        int count = 0;
        int maxOdd = Integer.MIN_VALUE;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (array[i] % 2 != 0) {
                count++;
                if (array[i] > maxOdd) {
                    maxOdd = array[i];
                }
            }
        }
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        System.out.println();
        System.out.println("Task completed in: " + duration +
                " nanoseconds (" + duration / 1000000 + " milliseconds)");
        System.out.println();

        System.out.println("Number of odd elements: " + count);
        if (maxOdd != Integer.MIN_VALUE) {
            System.out.println("Largest odd number: " + maxOdd);
        } else {
            System.out.println("No odd numbers found in array");
        }
    }
}