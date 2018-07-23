package icfpc2018.solver.construct;

import icfpc2018.solver.ReverseSolver;
import icfpc2018.solver.reconstruct.SingleBotReconstructSolver;
import org.junit.Before;

/**
 * @author ryosukek
 */

public class ReverseSingleBotConstructSolverTest extends BaseConstructSolverTest {
    @Before
    public void setUp() {
        solver = new ReverseSolver<>(
                new SingleBotReconstructSolver()
        );
    }
}
