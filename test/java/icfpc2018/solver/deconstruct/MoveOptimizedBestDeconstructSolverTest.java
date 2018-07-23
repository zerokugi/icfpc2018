package icfpc2018.solver.deconstruct;

import icfpc2018.solver.BestSolver;
import icfpc2018.solver.MoveOptimizedSolver;
import org.junit.Before;

public class MoveOptimizedBestDeconstructSolverTest extends BaseDeconstructSolverTest {
    @Before
    public void setUp() {
        solver = new MoveOptimizedSolver<>(new BestSolver());
    }
}
