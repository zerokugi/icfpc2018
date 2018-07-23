package icfpc2018.solver.all.single;

import icfpc2018.solver.all.BaseAllSolverTest;
import icfpc2018.solver.reconstruct.SingleBotReconstructSolver;
import org.junit.Before;

public class SingleBotAllSolverTest extends BaseAllSolverTest {
    @Before
    public void setUp() {
        solver = new SingleBotReconstructSolver();
    }
}
