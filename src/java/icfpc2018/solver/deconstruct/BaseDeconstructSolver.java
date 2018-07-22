package icfpc2018.solver.deconstruct;

import icfpc2018.models.Board;
import icfpc2018.models.Trace;

import java.util.List;

public abstract class BaseDeconstructSolver {

    public abstract List<Trace> solve(final Board initialBoard);
}
