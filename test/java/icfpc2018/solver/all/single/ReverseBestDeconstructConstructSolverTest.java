package icfpc2018.solver.all.single;

import icfpc2018.solver.construct.BaseConstructSolverTest;
import icfpc2018.solver.construct.ReverseBestDeconstructConstructSolver;
import org.junit.Before;

public class ReverseBestDeconstructConstructSolverTest extends BaseConstructSolverTest {
    @Before
    public void setUp() {
        solver = new ReverseBestDeconstructConstructSolver();
    }
}
