package icfpc2018.solver.construct;

import icfpc2018.solver.reconstruct.SingleBotReconstructSolver;
import org.junit.Before;

public class SingleBotConstructSolverTest extends BaseConstructSolverTest {
    @Before
    public void setUp() {
        solver = new SingleBotReconstructSolver();
    }
}
