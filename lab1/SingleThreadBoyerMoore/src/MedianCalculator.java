import java.util.Collections;
import java.util.Vector;

public class MedianCalculator {

    private Vector<Float> vector;

    public MedianCalculator(Vector<Float> vector) {
        this.vector = vector;
    }

    public float calculateMedian() {
        Collections.sort(vector);
        int vectorSize = vector.size();
        int middle = vectorSize / 2;
        if (vectorSize % 2 == 0) {
            return (vector.get(middle - 1) + vector.get(middle)) / 2;
        } else {
            return vector.get(middle);
        }
    }
}
