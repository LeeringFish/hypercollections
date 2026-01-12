package collections;

import java.util.Arrays;
import java.util.Objects;

public final class ImmutableCollection<T> {
    private final T[] elements;

    private ImmutableCollection(T[] elements) {
        this.elements = elements;
    }

    public static <T> ImmutableCollection<T> of() {
        @SuppressWarnings("unchecked")
        T[] empty = (T[]) new Object[0];
        return new ImmutableCollection<>(empty);
    }

    @SafeVarargs
    public static <T> ImmutableCollection<T> of(T...elements) {
        for (T element: elements) {
            if (Objects.isNull(element)) {
                throw new NullPointerException();
            }
        }
        return new ImmutableCollection<>(Arrays.copyOf(elements, elements.length));
    }

    public boolean contains(T item) {
        for (T element : elements) {
            if (Objects.equals(item, element)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        if (Objects.isNull(elements)) {
            return 0;
        }
        return elements.length;
    }

    public boolean isEmpty() {
        return Objects.isNull(elements) || elements.length == 0;
    }
}
