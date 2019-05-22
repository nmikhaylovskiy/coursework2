package sample;

public class MutableIntPair extends IntPair {

    public int first, second;

    public MutableIntPair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public static MutableIntPair empty() {
        return new MutableIntPair(-1, -1);
    }

    public void add(int val) {
        if (first == -1) {
            first = val;
            return;
        }
        if (second == -1) {
            second = val;
            return;
        }
        throw new IllegalArgumentException("p is already full");
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
