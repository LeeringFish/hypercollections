package collections;

public class Range<T extends Comparable<? super T>> {
    private final T elementA;
    private final T elementB;
    private final boolean leftClosed;
    private final boolean rightClosed;

    private Range(T a, T b, boolean leftClosed, boolean rightClosed) {
        this.elementA = a;
        this.elementB = b;
        this.leftClosed = leftClosed;
        this.rightClosed = rightClosed;
    }

    public static <T extends Comparable<? super T>> Range<T> open(T a, T b) {
        if (a == null || b == null) {
            throw new NullPointerException();
        }

        if (a.compareTo(b) >= 0) {
            throw new IllegalArgumentException();
        }

        return new Range<>(a, b, false, false);
    }

    public static <T extends Comparable<? super T>> Range<T> closed(T a, T b) {
        if (a == null || b == null) {
            throw new NullPointerException();
        }

        if (a.compareTo(b) > 0) {
            throw new IllegalArgumentException();
        }

        return new Range<>(a, b, true, true);
    }

    public boolean contains(T value) {
        if (value == null) {
            return false;
        }

        boolean leftOk = (elementA == null)
                || (leftClosed  ? value.compareTo(elementA) >= 0
                : value.compareTo(elementA) > 0);

        boolean rightOk = (elementB == null)
                || (rightClosed ? value.compareTo(elementB) <= 0
                : value.compareTo(elementB) < 0);

        return leftOk && rightOk;
    }

}
