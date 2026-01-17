package collections;

public class Range<T extends Comparable<? super T>> {
    private T elementA;
    private T elementB;
    private boolean leftClosed;
    private boolean rightClosed;

    private Range(T a, T b, boolean leftClosed, boolean rightClosed) {
        this.elementA = a;
        elementB = b;
        leftClosed = leftClosed;
        rightClosed = rightClosed;
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
}
