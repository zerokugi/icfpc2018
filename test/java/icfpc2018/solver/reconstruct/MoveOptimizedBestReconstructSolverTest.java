package icfpc2018.solver.reconstruct;

import icfpc2018.solver.BestSolver;
import icfpc2018.solver.MoveOptimizedSolver;
import org.junit.Before;

public class MoveOptimizedBestReconstructSolverTest extends BaseReconstructSolverTest {
    @Before
    public void setUp() {
        solver = new MoveOptimizedSolver<>(new BestSolver());
    }
}
