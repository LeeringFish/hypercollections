package collections;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Multiset<E> {
    private Map<E, Integer> bag;

    public Multiset() {
        bag = new LinkedHashMap<>();
    }

    public void add(E element, int occurrences) {
        if (occurrences > 0) {
            if (bag.containsKey(element)) {
                bag.put(element, bag.get(element) + occurrences);
            } else {
                bag.put(element, occurrences);
            }
        }
    }

    public void add(E element) {
        add(element, 1);
    }

    public boolean contains(E element) {
        return bag.containsKey(element);
    }

    public int count(E element) {
        if (bag.containsKey(element)) {
            return bag.get(element);
        }
        return 0;
    }

    public Set<E> elementSet() {
        return bag.keySet();
    }

    public void remove(E element, int occurrences) {
        if (occurrences > 0) {
            if (bag.containsKey(element)) {
                bag.put(element, bag.get(element) - occurrences);
                if (bag.get(element) <= 0) {
                    // try again
                    //bag.put(element, 0);
                    bag.remove(element);
                }
            }
        }
    }

    public void remove(E element) {
        remove(element, 1);
    }

    public void setCount(E element, int count) {
        if (count >= 0 && bag.containsKey(element)) {
            if (count == 0) {
                bag.remove(element);
                return;
            }

            bag.put(element, count);
        }
    }

    public void setCount(E element, int oldCount, int newCount) {
        if (bag.containsKey(element) && bag.get(element) == oldCount) {
            setCount(element, newCount);
        }
    }

    @Override
    public String toString() {
        if (bag.isEmpty()) {
            return "[]";
        }

        StringBuilder builder = new StringBuilder("[");
        for (Map.Entry<E, Integer> entry: bag.entrySet()) {
            E element = entry.getKey();
            int count = entry.getValue();
            for (int i = 0; i < count; i++) {
                builder.append(element).append(", ");
            }
        }

        builder.setLength(builder.length() - 2);
        builder.append("]");
        return builder.toString();
    }

}
