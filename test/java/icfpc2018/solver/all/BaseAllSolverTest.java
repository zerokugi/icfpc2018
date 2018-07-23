package icfpc2018.solver.all;

import com.google.common.collect.Streams;
import icfpc2018.solver.BaseSolverTest;
import org.junit.Ignore;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Ignore
public class BaseAllSolverTest extends BaseSolverTest {
    @Parameterized.Parameters(name = "{2}")
    public static Collection<Object[]> data() {
        return Streams.concat(
                IntStream.range(1, 187).mapToObj(i -> new Object[]{
                        null,
                        String.format("problemsF/FA%03d_tgt.mdl", i),
                        String.format("FA%03d", i)
                }),
                IntStream.range(1, 187).mapToObj(i -> new Object[]{
                        String.format("problemsF/FD%03d_src.mdl", i),
                        null,
                        String.format("FD%03d", i)
                }),
                IntStream.range(1, 116).mapToObj(i -> new Object[]{
                        String.format("problemsF/FR%03d_src.mdl", i),
                        String.format("problemsF/FR%03d_tgt.mdl", i),
                        String.format("FR%03d", i)
                })
        ).collect(Collectors.toList());
    }
}
