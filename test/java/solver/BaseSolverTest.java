package solver;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Ignore
@RunWith(Parameterized.class)
public class BaseSolverTest {

    public BaseSolver solver;
    @Parameterized.Parameter // first data value (0) is default
    public String path;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return IntStream.range(1, 187)
                .mapToObj(i -> new Object[]{String.format("problemsL/LA%03d_tgt.mdl", i)})
                .collect(Collectors.toList());
    }

    private static Board getState(final String path) throws IOException {
        final FileInputStream input = new FileInputStream(path);
        final byte[] Rb = new byte[1];
        assert input.read(Rb) == 1;
        final int R = Rb[0] & 0xff;
        final int readBytes = ((R * R * R) + 7) / 8;
        final byte[] board = new byte[readBytes];
        assert readBytes == input.read(board);
        input.close();
        return new Board(R, board, path);
    }

    @Test
    public void testTest1() throws IOException {
        final Board goal = getState(path);
        final List<Trace> traces = solver.solve(goal);
        assert (0 < goal.getR()) && (goal.getR() <= 250);
        assert traces != null;
//        final String types = traces.stream().map(trace -> trace.type.name()).distinct().collect(Collectors.joining());
//        System.out.println(types);
        final Game game = new Game(goal, traces);
        while (game.proceed()) {
//            System.out.printf("%d\n", game.getState().getEnergy());
        }
        assert game.validateSuccess() : "game not success";
        System.out.printf("%s: %d\n", goal.getPath(), game.getState().getEnergy());
    }
}
