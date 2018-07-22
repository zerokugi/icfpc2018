package icfpc2018.solver.deconstruct.reverse;

import icfpc2018.models.Board;
import icfpc2018.models.Trace;
import icfpc2018.solver.construct.BaseConstructSolver;
import icfpc2018.solver.deconstruct.BaseDeconstructSolver;

import java.util.ArrayList;
import java.util.List;

import static icfpc2018.models.Trace.Type.HALT;

/**
 * @author ryosukek
 */

public class ReverseDeconstructSolver<ConstructSolver extends BaseConstructSolver> extends BaseDeconstructSolver {
    final ConstructSolver constructSolver;

    public ReverseDeconstructSolver(final ConstructSolver constructSolver) {
        this.constructSolver = constructSolver;
    }

    static List<Trace> reverseTraces(final List<Trace> traces) {
        final int n = traces.size();
        if (traces.isEmpty()) {
            return traces;
        }
        assert (traces.get(n - 1).type == HALT);
        final List<Trace> reversedTraces = new ArrayList<>();
        for (int i = n - 2; i >= 0; i--) {
            reversedTraces.add(traces.get(i).reverse());
        }
        reversedTraces.add(new Trace(HALT));
        return reversedTraces;
    }

    @Override
    public List<Trace> solve(final Board initialBoard) {
        final List<Trace> constructTraces = constructSolver.solve(initialBoard);
        return reverseTraces(constructTraces);
    }
}
