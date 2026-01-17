package collections;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class SizeLimitedQueue<E> {
    private ArrayDeque<E> queue;
    private int capacity;

    public SizeLimitedQueue(int maxCapacity) {
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException();
        }

        queue = new ArrayDeque<>();
        capacity = maxCapacity;
    }

    public void add(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        if (isAtFullCapacity()) {
            queue.removeFirst();
        }

        queue.addLast(element);
    }

    public void clear() {
        queue.clear();
    }

    public boolean isAtFullCapacity() {
        return queue.size() == capacity;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int maxSize() {
        return capacity;
    }

    public E peek() {
        return queue.peek();
    }

    public E remove() {
        if (queue.isEmpty()) {
            throw new NoSuchElementException();
        }

        return queue.removeFirst();
    }

    public int size() {
        return queue.size();
    }

    public E[] toArray(E[] e) {
        return queue.toArray(e);
    }

    public Object[] toArray() {
        return queue.toArray(new Object[0]);
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder("[");
        Iterator<E> iter = queue.iterator();
        while (iter.hasNext()) {
            builder.append(iter.next());
            if (iter.hasNext()) {
                builder.append(", ");
            }
        }

        builder.append("]");
        return builder.toString();
    }
}