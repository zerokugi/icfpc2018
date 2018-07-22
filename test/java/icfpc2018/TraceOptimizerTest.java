package icfpc2018;

import icfpc2018.models.Board;
import icfpc2018.models.Coordinate;
import icfpc2018.models.Trace;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TraceOptimizerTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Board board;
    private TraceOptimizer traceOptimizer;

    @Before
    public void setUp(){
        board = Board.getInitialBoard(20);
        traceOptimizer = new TraceOptimizer(board.getR());
    }

    @Test
    public void testShortestPathFindSmove() {
        final List<Trace> traces = traceOptimizer.shortestPath(board, new Coordinate(0, 0, 0), new Coordinate(4, 0, 0));

        assertEquals(1, traces.size());
        assertEquals(Trace.Type.SMOVE, traces.get(0).type);
        assertEquals(1, (int) traces.get(0).val0);
        assertEquals(4 + 15, (int) traces.get(0).val1);
    }

    @Test
    public void testShortestPathFindLmoveWithVoxel() {
        board.fill(2, 0, 0);
        final List<Trace> traces = traceOptimizer.shortestPath(board, new Coordinate(0, 0, 0), new Coordinate(4, 3, 0));

        assertEquals(1, traces.size());
        assertEquals(Trace.Type.LMOVE, traces.get(0).type);
        assertEquals(2, (int) traces.get(0).val0);
        assertEquals(3 + 5, (int) traces.get(0).val1);
        assertEquals(1, (int) traces.get(0).val2);
        assertEquals(4 + 5, (int) traces.get(0).val3);
    }

    @Test
    public void testShortestPathFindLongPath() {
        board.fill(2, 0, 0);
        final List<Trace> traces = traceOptimizer.shortestPath(board, new Coordinate(0, 0, 0), new Coordinate(19, 3, 0));

        assertEquals(2, traces.size());
        assertEquals(Trace.Type.LMOVE, traces.get(0).type);
        assertEquals(2, (int) traces.get(0).val0);
        assertEquals(3 + 5, (int) traces.get(0).val1);
        assertEquals(1, (int) traces.get(0).val2);

        assertEquals(Trace.Type.SMOVE, traces.get(1).type);
        assertEquals(1, (int) traces.get(1).val0);
    }


    @Test
    public void testShortestPathFindLongestPath() {
        board.fill(2, 0, 0);
        board.fill(1, 0, 1);
        final List<Trace> traces = traceOptimizer.shortestPath(board, new Coordinate(1, 0, 0), new Coordinate(19, 3, 11));
        traces.add(0, new Trace(Trace.Type.SMOVE, 1, 1 + 15));
        final Game game = new Game(board, traces);
        game.getState().getBoard().fill(2, 0, 0);
        game.getState().getBoard().fill(1, 0, 1);

        for(int i = 0; i < traces.size(); i++) {
            game.proceed();
        }
        assertEquals(new Coordinate(19, 3, 11), game.getState().getBots().get(0).pos);
    }

    @Test
    public void testShortestPathNotFound() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("No way...");

        board.fill(0, 1, 0);
        board.fill(1, 0, 0);
        board.fill(0, 0, 1);
        traceOptimizer.shortestPath(board, new Coordinate(0, 0, 0), new Coordinate(10, 9, 3));
    }
}
