package icfpc2018.solver.reconstruct;

import icfpc2018.models.Board;
import icfpc2018.models.Trace;

import java.util.List;

public abstract class BaseReconstructSolver {

    public abstract List<Trace> solve(final Board initialBoard, final Board finalBoard);
}
