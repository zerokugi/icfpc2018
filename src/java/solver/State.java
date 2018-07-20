package solver;

public class State {
    private final int R;
    private final byte[] board;

    public State(final int r, final byte[] board) {
        R = r;
        this.board = board;
    }

    public int getR() {
        return R;
    }

    public boolean get(final int x, final int y, final int z) {
        assert (0 <= Math.min(x, Math.min(y, z))) && (Math.max(x, Math.max(y, z)) < R);
        final int pos = (x * R * R) + (y * R) + z;
        return ((board[pos >> 3] >> (pos & 7)) & 1) == 1;
    }
}
