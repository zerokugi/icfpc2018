package icfpc2018.models;

import org.junit.Test;

import static icfpc2018.models.Board.getInitialBoard;

/**
 * @author ryosukek
 */

public class BoardTest {
    @Test
    public void testGrounded() {
        final Board board = getInitialBoard(10);
        assert board.mustBeGrounded();
        board.fill(0, 0, 0);
        assert board.mustBeGrounded();
        board.fill(0, 1, 0);
        assert board.mustBeGrounded();
        board.fill(0, 3, 0);
        assert !board.mustBeGrounded();
        board.fill(0, 2, 0);
        assert board.mustBeGrounded();
        board.fill(1, 4, 0);
        assert !board.mustBeGrounded();
        board.fill(0, 4, 0);
        assert board.mustBeGrounded();
    }
}
