package solver;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Bot {
    public int bid;
    public Coordinate pos;
    public List<Integer> seeds;

    public Bot() {
        bid = 1;
        pos = new Coordinate(0,0,0);
        seeds = IntStream.rangeClosed(2, 20).boxed().collect(Collectors.toList());
    }
}
