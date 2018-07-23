package icfpc2018.solver.all.dflt;

import icfpc2018.solver.BestSolver;
import icfpc2018.solver.all.BaseAllSolverTest;
import org.junit.Before;

public class BestAllSolverTest extends BaseAllSolverTest {
    @Before
    public void setUp() {
        solver = new BestSolver();
    }
}
