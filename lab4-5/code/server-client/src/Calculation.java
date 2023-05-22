import java.util.*;
import java.util.concurrent.*;

public class Calculation implements Callable<Double[]> {

    private final ArrayList<Double> vector;
    private final int startIndex;
    private final int endIndex;
    private double mode;
    private double median;
    private Double counter = 0.0;

    public Calculation(ArrayList<Double> vector, int startIndex, int endIndex) {
        this.vector = vector;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public Double[] call() {
        return getModeMedian();
    }

    private void calcMedian() {
        List<Double> sublist = vector.subList(startIndex, endIndex);
        Double[] vectorChunk = sublist.toArray(new Double[0]);

        Arrays.sort(vectorChunk);
        int middle = vectorChunk.length / 2;
        if (vectorChunk.length % 2 == 0) {
            median = (vectorChunk[middle - 1] + vectorChunk[middle]) / 2.0;
        } else {
            median = vectorChunk[middle];
        }
    }

    private void calcMode() {
        List<Double> sublist = vector.subList(startIndex, endIndex); // Add 1 to include endIndex
        Double[] vectorChunk = sublist.toArray(new Double[0]);

        Map<Double, Double> freqMap = new HashMap<>();
        // Iterate through the array and update the frequency of each number in the HashMap
        for (double num : vectorChunk) {
            if (freqMap.containsKey(num)) {
                freqMap.put(num, freqMap.get(num) + 1.0);
            } else {
                freqMap.put(num, 1.0);
            }
        }

        // Find the mode and its frequency
        for (Map.Entry<Double, Double> entry : freqMap.entrySet()) {
            if (entry.getValue() > counter) {
                mode = entry.getKey();
                counter = entry.getValue();
            }
        }
    }

    private Double[] getModeMedian() {
        calcMode();
        calcMedian();
        return new Double[] {median, mode, counter};
    }
}
