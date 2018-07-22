package icfpc2018.solver.construct;

import icfpc2018.models.Board;
import icfpc2018.models.Trace;

import java.util.List;

public abstract class BaseConstructSolver {

    public abstract List<Trace> solve(final Board finalBoard);
}
