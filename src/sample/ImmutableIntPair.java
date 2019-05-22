package sample;

public class ImmutableIntPair extends IntPair {

    public final int first, second;

    public ImmutableIntPair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int getFirst() {
        return first;
    }

    @Override
    public int getSecond() {
        return second;
    }
}
