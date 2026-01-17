package collections;

import java.util.Objects;

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
        return between(a, b, false, false);
    }

    public static <T extends Comparable<? super T>> Range<T> closed(T a, T b) {
        return between(a, b, true, true);
    }

    public static <T extends Comparable<? super T>> Range<T> openClosed(T a, T b) {
        return between(a, b, false, true);
    }

    public static <T extends Comparable<? super T>> Range<T> closedOpen(T a, T b) {
        return between(a, b, true, false);
    }

    public static <T extends Comparable<? super T>> Range<T> greaterThan(T a) {
        return lower(a, false);
    }

    public static <T extends Comparable<? super T>> Range<T> atLeast(T a) {
        return lower(a,true);
    }

    public static <T extends Comparable<? super T>> Range<T> lessThan(T b) {
        return upper(b,false);
    }

    public static <T extends Comparable<? super T>> Range<T> atMost(T b) {
        return upper(b, true);
    }

    public static <T extends Comparable<? super T>> Range<T> all() {
        return new Range<>(null, null, false, false);
    }

    private static <T extends Comparable<? super T>> Range<T> between(T a, T b, boolean leftClosed, boolean rightClosed) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);

        int cmp = a.compareTo(b);
        if (cmp > 0 || (cmp == 0 && !leftClosed && !rightClosed)) {
            throw new IllegalArgumentException();
        }

        return new Range<>(a, b, leftClosed, rightClosed);
    }

    private static <T extends Comparable<? super T>> Range<T> lower(T a, boolean inclusive) {
        Objects.requireNonNull(a);
        return new Range<>(a, null, inclusive, false);
    }

    private static <T extends Comparable<? super T>> Range<T> upper(T b, boolean inclusive) {
        Objects.requireNonNull(b);
        return new Range<>(null, b, false, inclusive);
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
