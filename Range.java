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
            throw new NullPointerException();
        }

        boolean leftOk = (elementA == null)
                || (leftClosed  ? value.compareTo(elementA) >= 0
                : value.compareTo(elementA) > 0);

        boolean rightOk = (elementB == null)
                || (rightClosed ? value.compareTo(elementB) <= 0
                : value.compareTo(elementB) < 0);

        return leftOk && rightOk;
    }

    public boolean encloses(Range<T> other) {
        if (this.isEmpty()) {
            return false;
        }

        if (other.isEmpty()) {
            return true;
        }

        if (this.equals(other)) {
            return true;
        }

        boolean leftEncloses, rightEncloses;

        if (elementA == null) {
            leftEncloses = true;
        } else if (other.elementA == null) {
            return false;
        } else {
            if (this.leftClosed == other.leftClosed || leftClosed) {
                leftEncloses = this.elementA.compareTo(other.elementA) <= 0;
            } else {
                leftEncloses = this.elementA.compareTo(other.elementA) < 0;
            }
        }

        if (elementB == null) {
            rightEncloses = true;
        } else if (other.elementB == null) {
            return false;
        } else {
            if (this.rightClosed == other.rightClosed || rightClosed) {
                rightEncloses = this.elementB.compareTo(other.elementB) >= 0;
            } else {
                rightEncloses = this.elementB.compareTo(other.elementB) > 0;
            }
        }

        return leftEncloses && rightEncloses;
    }

    public Range<T> intersection(Range<T> connectedRange) {
        if (this.isEmpty()) {
            return this;
        }

        if (connectedRange.isEmpty()) {
            return connectedRange;
        }

        if (elementA == null && elementB == null) {
            return connectedRange;
        }
        if (connectedRange.elementA == null && connectedRange.elementB == null) {
            return this;
        }

        T newA, newB;
        boolean newLeftClosed, newRightClosed;

        if (this.elementA == null) {
            newA = connectedRange.elementA;
            newLeftClosed = connectedRange.leftClosed;
        } else if (connectedRange.elementA == null) {
            newA = this.elementA;
            newLeftClosed = this.leftClosed;
        } else {
            int c = this.elementA.compareTo(connectedRange.elementA);
            if (c > 0) {
                newA = this.elementA;
                newLeftClosed = this.leftClosed;
            } else if (c < 0) {
                newA = connectedRange.elementA;
                newLeftClosed = connectedRange.leftClosed;
            } else { // equal
                newA = this.elementA;
                newLeftClosed = this.leftClosed && connectedRange.leftClosed;
            }
        }

        if (this.elementB == null) {
            newB = connectedRange.elementB;
            newRightClosed = connectedRange.rightClosed;
        } else if (connectedRange.elementB == null) {         // +INF
            newB = this.elementB;
            newRightClosed = this.rightClosed;
        } else {
            int c = this.elementB.compareTo(connectedRange.elementB);
            if (c < 0) {
                newB = this.elementB;
                newRightClosed = this.rightClosed;
            } else if (c > 0) {
                newB = connectedRange.elementB;
                newRightClosed = connectedRange.rightClosed;
            } else { // equal
                newB = this.elementB;
                newRightClosed = this.rightClosed && connectedRange.rightClosed;
            }
        }

        if (newA != null && newB != null) {
            int cmp = newA.compareTo(newB);

            if (cmp > 0) {
                return new Range<>(newA, newA, false, true);
            }

            if (cmp == 0 && !(newLeftClosed && newRightClosed)) {
                return new Range<>(newA, newA, false, true);
            }
        }

        return new Range<>(newA, newB, newLeftClosed, newRightClosed);
    }

    public <U extends T> Range<T> span(Range<U> connectedRange) {
        if (connectedRange == null) {
            throw new NullPointerException();
        }

        if (this.isEmpty()) {
            return new Range<>(connectedRange.elementA, connectedRange.elementB,
                    connectedRange.leftClosed, connectedRange.rightClosed);
        }

        if (connectedRange.isEmpty()) {
            return new Range<>(elementA, elementB, leftClosed, rightClosed);
        }

        if (elementA == null && elementB == null) {
            return all();
        }
        if (connectedRange.elementA == null && connectedRange.elementB == null) {
            return all();
        }

        T newA = null, newB = null;
        boolean newLeftClosed = false, newRightClosed = false;
        boolean leftIsSet = false, rightIsSet = false;

        if (elementA == null) {
            newLeftClosed = leftClosed;
            leftIsSet = true;
        } else if (elementB == null) {
            newRightClosed = rightClosed;
            rightIsSet = true;
        }

        if (connectedRange.elementA == null) {
            newLeftClosed = connectedRange.leftClosed;
            leftIsSet = true;
        } else if (connectedRange.elementB == null) {
            newRightClosed = connectedRange.rightClosed;
            rightIsSet = true;
        }

        if (!leftIsSet && !rightIsSet) {
            if (elementA.compareTo(connectedRange.elementA) <= 0) {
                newA = elementA;
                if (elementA.compareTo(connectedRange.elementA) == 0) {
                    newLeftClosed = leftClosed || connectedRange.leftClosed;
                } else {
                    newLeftClosed = leftClosed;
                }

            } else {
                newA = connectedRange.elementA;
                newLeftClosed = connectedRange.leftClosed;
            }

            if (elementB.compareTo(connectedRange.elementB) >= 0) {
                newB = elementB;
                if (elementB.compareTo(connectedRange.elementB) == 0) {
                    newRightClosed = rightClosed || connectedRange.rightClosed;
                } else {
                    newRightClosed = rightClosed;
                }

            } else {
                newB = connectedRange.elementB;
                newRightClosed = connectedRange.rightClosed;
            }

        } else if (!leftIsSet) {
            if (elementA.compareTo(connectedRange.elementA) <= 0) {
                newA = elementA;
                if (elementA.compareTo(connectedRange.elementA) == 0) {
                    newLeftClosed = leftClosed || connectedRange.leftClosed;
                } else {
                    newLeftClosed = leftClosed;
                }
            } else {
                newA = connectedRange.elementA;
                newLeftClosed = connectedRange.leftClosed;
            }
        } else if (!rightIsSet) {
            if (elementB.compareTo(connectedRange.elementB) >= 0) {
                newB = elementB;
                if (elementB.compareTo(connectedRange.elementB) == 0) {
                    newRightClosed = rightClosed || connectedRange.rightClosed;
                } else {
                    newRightClosed = rightClosed;
                }
            } else {
                newB = connectedRange.elementB;
                newRightClosed = connectedRange.rightClosed;
            }
        }

        return new Range<>(newA, newB, newLeftClosed, newRightClosed);
    }

    public boolean isEmpty() {
        if (elementA == null || elementB == null) {
            return false;
        }

        return elementA.equals(elementB) && leftClosed != rightClosed;
    }

    private boolean equals(Range<T> other) {
        boolean leftEqual = Objects.equals(elementA, other.elementA);
        boolean rightEqual = Objects.equals(elementB, other.elementB);
        boolean leftClosedEqual = leftClosed == other.leftClosed;
        boolean rightClosedEqual = rightClosed == other.rightClosed;
        return leftEqual && rightEqual && leftClosedEqual && rightClosedEqual;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "EMPTY";
        }

        String left = leftClosed ? "[" : "(";
        String right = rightClosed ? "]" : ")";

        String first = elementA == null ? "-INF" : elementA.toString();
        String last = elementB == null ? "INF" : elementB.toString();

        return String.format("%s%s, %s%s", left, first, last, right);
    }

}
