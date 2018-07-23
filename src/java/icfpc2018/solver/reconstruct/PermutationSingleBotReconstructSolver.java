package icfpc2018.solver.reconstruct;

import com.google.common.collect.ImmutableList;
import icfpc2018.Game;
import icfpc2018.models.Board;
import icfpc2018.models.Trace;
import icfpc2018.solver.BaseSolver;

import java.util.List;

public class PermutationSingleBotReconstructSolver extends BaseSolver {
    private static final int[][] ALL_PERMUTATION = {
            {0, 1, 2, 3, 4, 5},
            {0, 1, 3, 2, 4, 5},
            {0, 2, 1, 3, 4, 5},
            {0, 2, 3, 1, 4, 5},
            {0, 3, 1, 2, 4, 5},
            {0, 3, 2, 1, 4, 5},

            {1, 0, 2, 3, 4, 5},
            {1, 0, 3, 2, 4, 5},
            {1, 2, 0, 3, 4, 5},
            {1, 2, 3, 0, 4, 5},
            {1, 3, 0, 2, 4, 5},
            {1, 3, 2, 0, 4, 5},

            {2, 0, 1, 3, 4, 5},
            {2, 0, 3, 1, 4, 5},
            {2, 1, 0, 3, 4, 5},
            {2, 1, 3, 0, 4, 5},
            {2, 3, 0, 1, 4, 5},
            {2, 3, 1, 0, 4, 5},

            {3, 0, 1, 2, 4, 5},
            {3, 0, 2, 1, 4, 5},
            {3, 1, 0, 2, 4, 5},
            {3, 1, 2, 0, 4, 5},
            {3, 2, 0, 1, 4, 5},
            {3, 2, 1, 0, 4, 5},
    };

    @Override
    public List<Trace> solve(final Board initialBoard, final Board finalBoard) {
        List<Trace> bestTraces = null;
        long best = -1;
        for (final int[] permutation : ALL_PERMUTATION) {
            final SingleBotReconstructSolver solver = new SingleBotReconstructSolver();
            solver.setPermutation(permutation);
            final List<Trace> traces = solver.solve(initialBoard.clone(), finalBoard.clone());
            final long energy = Game.getEnergy(initialBoard.clone(), finalBoard.clone(), ImmutableList.copyOf(traces));
            if (best == -1 || energy < best) {
                bestTraces = traces;
                best = energy;
            }
        }
        return bestTraces;
    }
}
