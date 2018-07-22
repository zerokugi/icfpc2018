package icfpc2018.solver.reconstruct.concatenate;

import icfpc2018.solver.construct.single.SingleBotConstructSolver;
import icfpc2018.solver.deconstruct.reverse.ReverseDeconstructSolver;
import icfpc2018.solver.reconstruct.BaseReconstructSolverTest;
import org.junit.Before;

public class ConcatenateReconstructSolverTest extends BaseReconstructSolverTest {
    @Before
    public void setUp() {
        solver = new ConcatenateReconstructSolver<>(
                new ReverseDeconstructSolver<>(new SingleBotConstructSolver()),
                new SingleBotConstructSolver()
        );
    }
}
