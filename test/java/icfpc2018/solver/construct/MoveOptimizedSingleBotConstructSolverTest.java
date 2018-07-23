package icfpc2018.solver.construct;

import icfpc2018.solver.MoveOptimizedSolver;
import icfpc2018.solver.reconstruct.SingleBotReconstructSolver;
import org.junit.Before;

public class MoveOptimizedSingleBotConstructSolverTest extends BaseConstructSolverTest {
    @Before
    public void setUp() {
        solver = new MoveOptimizedSolver<>(new SingleBotReconstructSolver());
    }
}
