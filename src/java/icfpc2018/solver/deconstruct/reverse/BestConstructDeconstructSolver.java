package icfpc2018.solver.deconstruct.reverse;

import icfpc2018.models.Board;
import icfpc2018.models.Trace;
import icfpc2018.solver.deconstruct.BaseDeconstructSolver;

import java.io.IOException;
import java.util.List;

import static icfpc2018.solver.construct.dflt.DefaultConstructSolver.convertTraces;
import static icfpc2018.solver.construct.dflt.DefaultConstructSolver.loadTraces;
import static icfpc2018.solver.deconstruct.reverse.ReverseDeconstructSolver.reverseTraces;

public class BestConstructDeconstructSolver extends BaseDeconstructSolver {
    @Override
    public List<Trace> solve(final Board initialBoard) {
        try {
            final byte[] rawTraces = loadTraces(
                    initialBoard.getPath()
                            .replace("problemsF", "dist/bestTraces")
                            .replace("FD", "FA")
                            .replace("_src.mdl", ".nbt")
            );
            return reverseTraces(convertTraces(rawTraces));
        } catch (IOException e) {
            throw new RuntimeException("error", e);
        }
    }
}
