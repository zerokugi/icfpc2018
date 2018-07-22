package icfpc2018.solver.deconstruct.dflt;

import icfpc2018.solver.deconstruct.BaseDeconstructSolverTest;
import icfpc2018.solver.deconstruct.reverse.BestConstructDeconstructSolver;
import org.junit.Before;

public class BestConstructDeconstructSolverTest extends BaseDeconstructSolverTest {
    @Before
    public void setUp() {
        solver = new BestConstructDeconstructSolver();
    }
}
