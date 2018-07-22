package icfpc2018.solver.deconstruct;

import icfpc2018.models.Board;
import icfpc2018.models.Trace;
import icfpc2018.solver.BaseSolver;

import java.util.List;

public abstract class BaseDeconstructSolver extends BaseSolver {

    public abstract List<Trace> solve(final Board initialBoard);

    public List<Trace> solve(final Board initialBoard, final Board finalBoard) {
        assert (finalBoard == null) || (finalBoard.getFilledCound() == 0);
        return solve(initialBoard);
    }
}

