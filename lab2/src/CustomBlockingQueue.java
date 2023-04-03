import java.util.LinkedList;
import java.util.Queue;

public class CustomBlockingQueue<T> {
    //Наприклад, якщо ви створюєте екземпляр
    // CustomBlockingQueue<Integer>,
    // то тип T заміниться на Integer,
    // і CustomBlockingQueue<Integer> буде
    // працювати з об'єктами типу Integer.
    private final Queue<T> queue;

    public CustomBlockingQueue() {
        this.queue = new LinkedList<>();
    }

    public synchronized void put(T element) {
        boolean wasEmpty = queue.isEmpty();
        queue.offer(element);
        if (wasEmpty) {
            notifyAll();
        }
    }

    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T element = queue.poll();
        notifyAll();
        return element;
    }

    public synchronized void clear() {
        queue.clear();
        notifyAll();
    }
}
