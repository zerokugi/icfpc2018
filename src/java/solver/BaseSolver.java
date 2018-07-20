package solver;

import java.util.List;

public abstract class BaseSolver {

    public abstract List<Trace> solve(final State finalState);
}