package icfpc2018.solver.construct;

import icfpc2018.models.Board;
import icfpc2018.models.Trace;
import icfpc2018.solver.BaseSolver;

import java.util.List;

public abstract class BaseConstructSolver extends BaseSolver {

    public abstract List<Trace> solve(final Board finalBoard);

    public List<Trace> solve(final Board initialBoard, final Board finalBoard) {
        assert (initialBoard == null) || (initialBoard.getFilledCound() == 0);
        return solve(finalBoard);
    }
}
