package icfpc2018.solver.construct;

import icfpc2018.models.Board;
import icfpc2018.models.Trace;

import java.io.IOException;
import java.util.List;

import static icfpc2018.solver.DefaultSolver.convertTraces;
import static icfpc2018.solver.DefaultSolver.loadTraces;
import static icfpc2018.solver.ReverseSolver.reverseTraces;

public class ReverseBestDeconstructConstructSolver extends BaseConstructSolver {
    @Override
    public List<Trace> solve(final Board initialBoard) {
        try {
            final byte[] rawTraces = loadTraces(
                    initialBoard.getPath()
                            .replace("problemsF", "dist/bestTraces")
                            .replace("FA", "FD")
                            .replace("_tgt.mdl", ".nbt")
            );
            return reverseTraces(convertTraces(rawTraces));
        } catch (IOException e) {
            throw new RuntimeException("error", e);
        }
    }
}
