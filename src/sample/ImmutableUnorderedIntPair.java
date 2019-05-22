package sample;

public class ImmutableUnorderedIntPair extends ImmutableIntPair {

    public ImmutableUnorderedIntPair(int first, int second) {
        super(first, second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntPair intPair = (IntPair) o;
        return getFirst() == intPair.getFirst() && getSecond() == intPair.getSecond()
                || getFirst() == intPair.getSecond() && getSecond() == intPair.getFirst();
    }

    @Override
    public int hashCode() {
        return 31 * Math.min(getFirst(), getSecond()) + Math.max(getFirst(), getSecond());
    }
}
