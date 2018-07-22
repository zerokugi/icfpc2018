package icfpc2018.solver.deconstruct.reverse;

import icfpc2018.solver.construct.single.SingleBotConstructSolver;
import icfpc2018.solver.deconstruct.BaseDeconstructSolverTest;
import org.junit.Before;

/**
 * @author ryosukek
 */

public class ReverseSingleBotDeconstructSolverTest extends BaseDeconstructSolverTest {
    @Before
    public void setUp() {
        solver = new ReverseDeconstructSolver<>(
                new SingleBotConstructSolver()
        );
    }
}
