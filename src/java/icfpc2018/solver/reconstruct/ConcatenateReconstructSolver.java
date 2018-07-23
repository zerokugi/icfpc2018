package icfpc2018.solver.reconstruct;

import icfpc2018.models.Board;
import icfpc2018.models.Trace;
import icfpc2018.solver.BaseSolver;

import java.util.ArrayList;
import java.util.List;

import static icfpc2018.models.Trace.Type.HALT;

public class ConcatenateReconstructSolver<
        DeconstructSolver extends BaseSolver,
        ConstructSolver extends BaseSolver
        > extends BaseSolver {
    private final DeconstructSolver deconstructSolver;
    private final ConstructSolver constructSolver;

    public ConcatenateReconstructSolver(final DeconstructSolver deconstructSolver, final ConstructSolver constructSolver) {
        this.deconstructSolver = deconstructSolver;
        this.constructSolver = constructSolver;
    }

    private List<Trace> concatenateTraces(final List<Trace> trace1, final List<Trace> trace2) {
        if (trace1.isEmpty()) {
            return trace2;
        }
        if (trace2.isEmpty()) {
            return trace1;
        }
        assert trace1.get(trace1.size() - 1).type == HALT;
        final List<Trace> concatenatedTraces = new ArrayList<>();
        concatenatedTraces.addAll(trace1.subList(0, trace1.size() - 1));
        concatenatedTraces.addAll(trace2);
        return concatenatedTraces;
    }

    @Override
    public List<Trace> solve(final Board initialBoard, final Board finalBoard) {
        final List<Trace> deconstructTraces = deconstructSolver.solve(initialBoard, Board.getInitialBoard(initialBoard.getR()));
        final List<Trace> constructTraces = constructSolver.solve(Board.getInitialBoard(finalBoard.getR()), finalBoard);
        return concatenateTraces(deconstructTraces, constructTraces);
    }
}
