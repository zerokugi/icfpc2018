package solver;

public class State {
    private final int R;
    private final byte[] board;
    private final String path;

    public State(final int r, final byte[] board, final String path) {
        R = r;
        this.board = board;
        this.path = path;
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

    public String getPath() {
        return path;
    }

    public boolean grounded() {
        // TODO
        return true;
    }
}
