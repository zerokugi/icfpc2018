package icfpc2018.solver;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import icfpc2018.models.Board;
import icfpc2018.models.Trace;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.io.IOException;
import java.util.List;

import static icfpc2018.solver.DefaultSolver.convertTraces;
import static icfpc2018.solver.DefaultSolver.loadTraces;

@Ignore
public class BestSolver extends BaseSolver {
    @Override
    public List<Trace> solve(final Board initialBoard, final Board finalBoard) {
        try {
            final byte[] rawTraces = loadTraces(
                    MoreObjects.firstNonNull(
                            Strings.emptyToNull(initialBoard.getPath()),
                            Strings.emptyToNull(finalBoard.getPath())
                    )
                            .replace("problemsF", "dist/bestTraces")
                            .replace("_src.mdl", ".nbt")
                            .replace("_tgt.mdl", ".nbt")
            );
            return convertTraces(rawTraces);
        } catch (IOException e) {
            throw new RuntimeException("error", e);
        }
    }
}
