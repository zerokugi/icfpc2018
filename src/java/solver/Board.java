package solver;

import com.google.common.base.Objects;

public class Board {
    private final int R;
    private final byte[] board;
    private final String path;

    public Board(final int r, final byte[] board, final String path) {
        R = r;
        this.board = board;
        this.path = path;
    }

    public static Board getInitialBoard(final int R) {
        int boardSize = (R * R * R + 7) / 8;
        return new Board(R, new byte[boardSize], "");
    }

    public byte[] getBoard() {
        return board;
    }

    public int getR() {
        return R;
    }

    private int getPos(final int x, final int y, final int z) {
        return (x * R * R) + (y * R) + z;
    }

    public boolean get(final int x, final int y, final int z) {
        assert (0 <= Math.min(x, Math.min(y, z))) && (Math.max(x, Math.max(y, z)) < R);
        final int pos = getPos(x, y, z);
        return ((board[pos >> 3] >> (pos & 7)) & 1) == 1;
    }

    public boolean fill(final int x, final int y, final int z) {
        assert (0 <= Math.min(x, Math.min(y, z))) && (Math.max(x, Math.max(y, z)) < R);
        final int pos = getPos(x, y, z);
        if (((board[pos >> 3] >> (pos & 7)) & 1) == 1) {
            return false;
        }
        board[pos >> 3] |= 1 << (pos & 7);
        return true;
    }

    public boolean fill(final Coordinate p) {
        return fill(p.x, p.y, p.z);
    }

    public String getPath() {
        return path;
    }

    public boolean grounded() {
        // TODO
        return true;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Board board1 = (Board) o;
        if (R != board1.R) {
            return false;
        }
        for (int i = 0; i < board.length; i++) {
            if (board[i] != board1.board[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(R, board);
    }
}
