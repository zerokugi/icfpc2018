package icfpc2018.solver.all;

import icfpc2018.solver.ReverseSolver;
import icfpc2018.solver.reconstruct.ConcatenateReconstructSolver;
import icfpc2018.solver.reconstruct.SingleBotReconstructSolver;
import org.junit.Before;

public class ConcatenatedReversedHorizontalPlaneCuttingSingleBotAllSolverTest extends BaseAllSolverTest {
    @Before
    public void setUp() {
        solver = new ConcatenateReconstructSolver<>(
                new ReverseSolver<>(
                        new SingleBotReconstructSolver()
                ),
                new SingleBotReconstructSolver()
        );
    }
}
