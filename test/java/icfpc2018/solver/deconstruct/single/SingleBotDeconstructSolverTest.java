package icfpc2018.solver.deconstruct.single;

import icfpc2018.solver.deconstruct.BaseDeconstructSolverTest;
import icfpc2018.solver.reconstruct.SingleBotReconstructSolver;
import org.junit.Before;

public class SingleBotDeconstructSolverTest extends BaseDeconstructSolverTest {
    @Before
    public void setUp() {
        solver = new SingleBotReconstructSolver();
    }
}
