package solver;

import org.junit.Test;

import static solver.Board.getInitialBoard;

/**
 * @author ryosukek
 */

public class BoardTest {
    @Test
    public void testGrounded() {
        final Board board = getInitialBoard(10);
        assert board.grounded();
        board.fill(0, 0, 0);
        assert board.grounded();
        board.fill(0, 1, 0);
        assert board.grounded();
        board.fill(0, 3, 0);
        assert !board.grounded();
        board.fill(0, 2, 0);
        assert board.grounded();
        board.fill(1, 4, 0);
        assert !board.grounded();
        board.fill(0, 4, 0);
        assert board.grounded();
    }
}
