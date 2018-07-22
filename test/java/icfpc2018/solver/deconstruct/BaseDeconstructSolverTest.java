package icfpc2018.solver.deconstruct;

import icfpc2018.solver.BaseSolverTest;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Ignore
public class BaseDeconstructSolverTest extends BaseSolverTest {
    @Parameterized.Parameters(name = "{2}")
    public static Collection<Object[]> data() {
        return IntStream.range(1, 187)
                .mapToObj(i -> new Object[]{String.format("problemsF/FD%03d_src.mdl", i), null, String.format("FD%03d", i)})
                .collect(Collectors.toList());
    }
}
