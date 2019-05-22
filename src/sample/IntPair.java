package sample;

public abstract class IntPair {

    abstract public int getFirst();
    abstract public int getSecond();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntPair intPair = (IntPair) o;
        return getFirst() == intPair.getFirst() && getSecond() == intPair.getSecond();
    }

    @Override
    public int hashCode() {
        return 31 * getFirst() + getSecond();
    }
}
