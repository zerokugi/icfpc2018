package icfpc2018.solver.construct;

import icfpc2018.solver.BaseSolverTest;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Ignore
@RunWith(Parameterized.class)
public class BaseLightningConstructSolverTest extends BaseSolverTest {
    @Parameterized.Parameters(name = "{2}")
    public static Collection<Object[]> data() {
        return IntStream.range(1, 187)
                .mapToObj(i -> new Object[]{null, String.format("problemsL/LA%03d_tgt.mdl", i), String.format("LA%03d", i)})
                .collect(Collectors.toList());
    }
}
