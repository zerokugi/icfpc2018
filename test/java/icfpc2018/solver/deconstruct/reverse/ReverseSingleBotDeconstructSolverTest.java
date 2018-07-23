package icfpc2018.solver.deconstruct.reverse;

import icfpc2018.solver.ReverseSolver;
import icfpc2018.solver.deconstruct.BaseDeconstructSolverTest;
import icfpc2018.solver.reconstruct.SingleBotReconstructSolver;
import org.junit.Before;

/**
 * @author ryosukek
 */

public class ReverseSingleBotDeconstructSolverTest extends BaseDeconstructSolverTest {
    @Before
    public void setUp() {
        solver = new ReverseSolver<>(
                new SingleBotReconstructSolver()
        );
    }
}
