package icfpc2018.solver.all;

import icfpc2018.solver.MoveOptimizedSolver;
import icfpc2018.solver.ReverseSolver;
import icfpc2018.solver.reconstruct.ConcatenateReconstructSolver;
import icfpc2018.solver.reconstruct.PermutationSingleBotReconstructSolver;
import org.junit.Before;

public class MoveOptimizedConcatenatedReversedHorizontalPlaneCuttingPermutationSingleBotAllSolverTest extends BaseAllSolverTest {
    @Before
    public void setUp() {
        solver = new MoveOptimizedSolver<>(
                new ConcatenateReconstructSolver<>(
                        new ReverseSolver<>(
                                new PermutationSingleBotReconstructSolver()
                        ),
                        new PermutationSingleBotReconstructSolver()
                )
        );
    }
}
