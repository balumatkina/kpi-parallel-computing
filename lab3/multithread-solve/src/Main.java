import java.util.Random;

public class Main {
    public static final int ARRAY_SIZE = 1000000;
    public static final int MAX_ARRAY_ELEMENT_VALUE = 8000;

    public static void main(String[] args) {
        Random random = new Random();

        long startTime = System.nanoTime();
        int[] array = new int[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = random.nextInt(MAX_ARRAY_ELEMENT_VALUE + 1);
        }

        OddNumberCounter counter = new OddNumberCounter(array);
        counter.countOdds();

        long endTime = System.nanoTime();
        System.out.println("Task completed in " + (endTime - startTime) / 1000000 + " milliseconds");
//        System.out.println("Task completed in " + counter. + " milliseconds");

        System.out.println("Odd count: " + counter.getOddCount());
        System.out.println("Max odd: " + counter.getMaxOdd());
    }
}