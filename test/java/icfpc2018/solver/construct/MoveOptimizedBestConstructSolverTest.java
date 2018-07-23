package icfpc2018.solver.construct;

import icfpc2018.solver.BestSolver;
import icfpc2018.solver.MoveOptimizedSolver;
import org.junit.Before;

public class MoveOptimizedBestConstructSolverTest extends BaseConstructSolverTest {
    @Before
    public void setUp() {
        solver = new MoveOptimizedSolver<>(new BestSolver());
    }
}
