package icfpc2018.solver.reconstruct.single;

import icfpc2018.solver.MoveOptimizedSolver;
import icfpc2018.solver.reconstruct.BaseReconstructSolverTest;
import icfpc2018.solver.reconstruct.SingleBotReconstructSolver;
import org.junit.Before;

public class MoveOptimizedSingleBotReconstructSolverTest extends BaseReconstructSolverTest {
    @Before
    public void setUp() {
        solver = new MoveOptimizedSolver<>(
                new SingleBotReconstructSolver()
        );
    }
}
