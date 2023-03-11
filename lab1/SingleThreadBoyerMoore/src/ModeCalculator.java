import java.util.Vector;

public class ModeCalculator {

    private Vector<Float> vector;

    public ModeCalculator(Vector<Float> vector) {
        this.vector = vector;
    }

    public float calculateMode() {
        float candidate = 0;
        int count = 0;
        for (float value : vector) {
            if (count == 0) {
                candidate = value;
                count = 1;
            } else if (value == candidate) {
                count++;
            } else {
                count--;
            }
        }
        return candidate;
    }
}
