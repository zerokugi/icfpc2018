package solver;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TraceTest {


    private Board board;

    @Before
    public void setUp() {
        board = Board.getInitialBoard(3);
    }

    @Test
    public void testFlip() {
        final List<Trace> traces = ImmutableList.of(new Trace(Trace.Type.FLIP));
        final Game game = new Game(board, traces);
        assertTrue(game.proceed());
        assertEquals(State.Harmonics.HIGH, game.getState().getHarmonics());
    }

    @Test
    public void testFillSuccess() {
        final List<Trace> traces = ImmutableList.of(Coordinate.toNldTrace(Trace.Type.FILL, new Coordinate(1, 0, 0)));
        final Game game = new Game(board, traces);
        assertTrue(game.proceed());
        assertTrue(game.getState().getBoard().get(1, 0, 0));
    }

    @Test(expected = AssertionError.class)
    public void testFillFailureByOutOfBounds() {
        final List<Trace> traces = ImmutableList.of(Coordinate.toNldTrace(Trace.Type.FILL, new Coordinate(-1, 0, 0)));
        final Game game = new Game(board, traces);
        game.proceed();
    }

    @Test(expected = AssertionError.class)
    public void testFillFailureByBot() {
        final List<Trace> traces = ImmutableList.of(Coordinate.toNldTrace(Trace.Type.FILL, new Coordinate(1, 0, 0)), new Trace(Trace.Type.WAIT));
        final Game game = new Game(board, traces);
        addBot(game, new Coordinate(1, 0, 0));
        game.proceed();
    }

    @Test
    public void testSmoveSuccess() {
        final List<Trace> traces = ImmutableList.of(Coordinate.toSmove(new Coordinate(2, 0, 0)));
        final Game game = new Game(board, traces);
        assertTrue(game.proceed());
    }

    @Test(expected = AssertionError.class)
    public void testSmoveFailureByFilledVoxel() {
        final List<Trace> traces = ImmutableList.of(Coordinate.toSmove(new Coordinate(2, 0, 0)));
        board.fill(1, 0, 0);
        final Game game = new Game(board, traces);
        game.proceed();
    }

    @Test(expected = AssertionError.class)
    public void testSmoveFailureByOutOfBounds() {
        final List<Trace> traces = ImmutableList.of(Coordinate.toSmove(new Coordinate(4, 0, 0)));
        final Game game = new Game(board, traces);
        game.proceed();
    }

    @Test(expected = AssertionError.class)
    public void testSmoveFailureByBot() {
        final List<Trace> traces = ImmutableList.of(Coordinate.toSmove(new Coordinate(2, 0, 0)), Trace.getWait());
        final Game game = new Game(board, traces);
        addBot(game, new Coordinate(1, 0, 0));
        game.proceed();
    }

    @Test
    public void testLmoveSuccess() {
        final List<Trace> traces = ImmutableList.of(Coordinate.toLmove(new Coordinate(2, 0, 0), new Coordinate(0, 1, 0)));
        final Game game = new Game(board, traces);
        assertTrue(game.proceed());
        assertEquals(game.getState().getBots().get(0).pos, new Coordinate(2, 1, 0));
    }

    @Test(expected = AssertionError.class)
    public void testLmoveFailureByVoxel() {
        final List<Trace> traces = ImmutableList.of(Coordinate.toLmove(new Coordinate(2, 0, 0), new Coordinate(0, 1, 0)));
        board.fill(2, 1, 0);
        final Game game = new Game(board, traces);
        game.proceed();
    }

    @Test(expected = AssertionError.class)
    public void testLmoveFailureByBot() {
        final List<Trace> traces = ImmutableList.of(Coordinate.toLmove(new Coordinate(2, 0, 0), new Coordinate(0, 1, 0)), new Trace(Trace.Type.WAIT));
        final Game game = new Game(board, traces);
        addBot(game, new Coordinate(2, 1, 0));
        game.proceed();
    }

    private void addBot(Game game, Coordinate c) {
        game.getState().getBots().add(new Bot(21, c, ImmutableList.of()));
    }

    @Test(expected = AssertionError.class)
    public void testLmoveFailureByOutOfBounds() {
        final List<Trace> traces = ImmutableList.of(Coordinate.toLmove(new Coordinate(2, 0, 0), new Coordinate(0, -1, 0)));
        final Game game = new Game(board, traces);
        game.proceed();
    }

    @Test
    public void testFissionSuccess() {
        final List<Trace> traces = ImmutableList.of(Coordinate.toNldTrace(Trace.Type.FISSION, new Coordinate(1, 0, 0), 3));
        final Game game = new Game(board, traces);
        assertTrue(game.proceed());
        assertEquals(2, game.getState().getBots().size());
        assertEquals(ImmutableList.of(3, 4, 5), game.getState().getBots().get(1).seeds);
        assertEquals(new Coordinate(1, 0, 0), game.getState().getBots().get(1).pos);
    }

    @Test(expected = AssertionError.class)
    public void testFissionFailureByOutOfBounds() {
        final List<Trace> traces = ImmutableList.of(Coordinate.toNldTrace(Trace.Type.FISSION, new Coordinate(-1, 0, 0), 3));
        final Game game = new Game(board, traces);
        game.proceed();
    }

    @Test(expected = AssertionError.class)
    public void testFissionFailureByVoxel() {
        final List<Trace> traces = ImmutableList.of(Coordinate.toNldTrace(Trace.Type.FISSION, new Coordinate(1, 0, 0), 3));
        board.fill(1, 0, 0);
        final Game game = new Game(board, traces);
        game.proceed();
    }

    @Test(expected = AssertionError.class)
    public void testFissionFailureByBot() {
        final List<Trace> traces = ImmutableList.of(Coordinate.toNldTrace(Trace.Type.FISSION, new Coordinate(-1, 0, 0), 3), new Trace(Trace.Type.WAIT));
        final Game game = new Game(board, traces);
        addBot(game, new Coordinate(1, 0, 0));
        game.proceed();
    }

}
