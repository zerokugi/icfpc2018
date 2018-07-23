package icfpc2018.solver.all;

import icfpc2018.solver.MoveOptimizedSolver;
import icfpc2018.solver.ReverseSolver;
import icfpc2018.solver.reconstruct.ConcatenateReconstructSolver;
import icfpc2018.solver.reconstruct.SingleBotReconstructSolver;
import org.junit.Before;

public class MoveOptimizedConcatenatedReversedHorizontalPlaneCuttingSingleBotAllSolverTest extends BaseAllSolverTest {
    @Before
    public void setUp() {
        solver = new MoveOptimizedSolver<>(
                new ConcatenateReconstructSolver<>(
                        new ReverseSolver<>(
                                new SingleBotReconstructSolver()
                        ),
                        new SingleBotReconstructSolver()
                )
        );
    }
}
