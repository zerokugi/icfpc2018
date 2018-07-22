package icfpc2018.solver.reconstruct.concatenate;

import icfpc2018.solver.construct.single.MoveOptimizedConstructSolver;
import icfpc2018.solver.construct.single.SingleBotConstructSolver;
import icfpc2018.solver.deconstruct.reverse.ReverseDeconstructSolver;
import icfpc2018.solver.reconstruct.BaseReconstructSolverTest;
import org.junit.Before;

public class MoveOptimizedConcatenateSingleBotReconstructSolverTest extends BaseReconstructSolverTest {
    @Before
    public void setUp() {
        solver = new ConcatenateReconstructSolver<>(
                new ReverseDeconstructSolver<>(
                        new MoveOptimizedConstructSolver<>(new SingleBotConstructSolver())
                ),
                new MoveOptimizedConstructSolver<>(new SingleBotConstructSolver())
        );
    }
}
