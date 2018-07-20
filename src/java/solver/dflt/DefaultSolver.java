package solver.dflt;

import solver.BaseSolver;
import solver.State;
import solver.Trace;

import java.util.ArrayList;
import java.util.List;

public class DefaultSolver extends BaseSolver {
    @Override
    public List<Trace> solve(final State finalState) {
        return new ArrayList<>();
    }
}
