package icfpc2018.solver.deconstruct.dflt;

import icfpc2018.models.Board;
import icfpc2018.models.Trace;
import icfpc2018.solver.deconstruct.BaseDeconstructSolver;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.io.IOException;
import java.util.List;

import static icfpc2018.solver.construct.dflt.DefaultConstructSolver.convertTraces;
import static icfpc2018.solver.construct.dflt.DefaultConstructSolver.loadTraces;

@Ignore
public class DefaultDeconstructSolver extends BaseDeconstructSolver {
    @Override
    public List<Trace> solve(final Board initialBoard) {
        try {
            final byte[] rawTraces = loadTraces(
                    initialBoard.getPath()
                            .replace("problems", "dfltTraces")
                            .replace("_src.mdl", ".nbt")
            );
            return convertTraces(rawTraces);
        } catch (IOException e) {
            throw new RuntimeException("error", e);
        }
    }
}
