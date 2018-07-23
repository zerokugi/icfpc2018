package icfpc2018.solver;

import icfpc2018.models.Board;
import icfpc2018.models.Trace;

import java.util.ArrayList;
import java.util.List;

import static icfpc2018.models.Trace.Type.HALT;

/**
 * @author ryosukek
 */

public class ReverseSolver<Solver extends BaseSolver> extends BaseSolver {
    final Solver solver;

    public ReverseSolver(final Solver solver) {
        this.solver = solver;
    }

    public static List<Trace> reverseTraces(final List<Trace> traces) {
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
    public List<Trace> solve(final Board initialBoard, final Board finalBoard) {
        final List<Trace> traces = solver.solve(finalBoard, initialBoard);
        return reverseTraces(traces);
    }
}
