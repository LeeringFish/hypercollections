package collections;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BiMap<K, V> {
    private Map<K, V> forwardMap;
    private Map<V, K> reverseMap;

    public BiMap() {
        forwardMap = new HashMap<>();
        reverseMap = new HashMap<>();
    }

    public V put(K key, V value) {
        if (forwardMap.containsKey(key) || reverseMap.containsKey(value)) {
            throw new IllegalArgumentException();
        }

        forwardMap.put(key, value);
        reverseMap.put(value, key);
        return value;
    }

    public void putAll(Map<K, V> map) {
        for (K key : map.keySet()) {
            if (forwardMap.containsKey(key) || reverseMap.containsKey(map.get(key))) {
                throw new IllegalArgumentException();
            }
        }
        for (K key : map.keySet()) {
            reverseMap.put(map.get(key), key);
        }
        forwardMap.putAll(map);
    }

    public Set<V> values() {
        return reverseMap.keySet();
    }

    public V forcePut(K key, V value) {
        boolean hadKey = forwardMap.containsKey(key);
        V oldValue = forwardMap.get(key);
        boolean hadValue = reverseMap.containsKey(value);
        K oldKey = reverseMap.get(value);

        if (hadValue && !Objects.equals(oldKey, key)) {
            forwardMap.remove(oldKey);
            reverseMap.remove(value);
        }

        if (hadKey) {
            reverseMap.remove(oldValue);
        }

        forwardMap.put(key, value);
        reverseMap.put(value, key);
        return oldValue;
    }

    public BiMap<V, K> inverse() {
        BiMap<V, K> inverseMap = new BiMap<V, K>();
        for (V value : reverseMap.keySet()) {
            inverseMap.put(value, reverseMap.get(value));
        }
        return inverseMap;
    }

    public String toString() {
        if (forwardMap.isEmpty()) {
            return "{}";
        }

        StringBuilder builder = new StringBuilder("{");
        for (K key : forwardMap.keySet()) {
            builder.append(key);
            builder.append("=");
            builder.append(forwardMap.get(key));
            builder.append(", ");
        }
        builder.replace(builder.length() - 2, builder.length(), "}");
        return builder.toString();
    }
}
