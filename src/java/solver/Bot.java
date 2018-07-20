package solver;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Bot implements Comparable {
    public int bid;
    public Coordinate pos;
    public List<Integer> seeds;

    public Bot() {
        bid = 1;
        pos = new Coordinate(0,0,0);
        seeds = IntStream.rangeClosed(2, 20).boxed().collect(Collectors.toList());
    }

    @Override
    public int compareTo(final Object o) {
        return bid - ((Bot) o).bid;
    }
}
