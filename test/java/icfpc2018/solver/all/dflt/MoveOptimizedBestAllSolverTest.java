package icfpc2018.solver.all.dflt;

import icfpc2018.solver.BestSolver;
import icfpc2018.solver.MoveOptimizedSolver;
import icfpc2018.solver.all.BaseAllSolverTest;
import org.junit.Before;

public class MoveOptimizedBestAllSolverTest extends BaseAllSolverTest {
    @Before
    public void setUp() {
        solver = new MoveOptimizedSolver<>(new BestSolver());
    }
}
