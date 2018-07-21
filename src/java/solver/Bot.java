package solver;

import java.util.List;

public class Bot {
    public Coordinate pos;
    public int bid;
    public List<Integer> seeds;
    public Trace assignedTrace;

    public Bot(final int bid, final Coordinate pos, final List<Integer> seeds) {
        this.bid = bid;
        this.pos = pos;
        this.seeds = seeds;
    }

    public Trace getAssignedTrace() {
        return assignedTrace;
    }

    public void assignTrace(final Trace assignedTrace) {
        this.assignedTrace = assignedTrace;
    }
}
