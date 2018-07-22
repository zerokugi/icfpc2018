package icfpc2018.models;

import com.google.common.base.Objects;
import icfpc2018.UnionFind;

public class Board {
    private final int R;
    private final byte[] board;
    private final String path;
    private final UnionFind unionFind;
    private int filledCound = 0;
    private boolean forceGrounded = false;

    public Board(final int r, final byte[] board, final String path) {
        R = r;
        this.path = path;
        unionFind = new UnionFind((r * r * r) + 1);
        this.board = new byte[R * R * R];
        for (int x = 1; x < R; x ++) {
            for (int y = 0; y < R; y ++) {
                for (int z = 1; z < R; z++) {
                    int pos = getPos(x, y, z);
                    if ((board[pos >> 3] & (1 << (pos & 7))) != 0) {
                        fill(x, y, z);
                    }
                }
            }
        }
    }

    public static Board getInitialBoard(final int R) {
        final int boardSize = ((R * R * R) + 7) / 8;
        return new Board(R, new byte[boardSize], "");
    }

    public Board clone() {
        return new Board(R, board, path);
    }

    public byte[] getBoard() {
        return board;
    }

    public int getR() {
        return R;
    }

    public int getPos(final int x, final int y, final int z) {
        return (x * R * R) + (y * R) + z;
    }

    public int getPos(final Coordinate p) {
        return getPos(p.x, p.y, p.z);
    }
    public Coordinate fromPos(final int pos) {
        return new Coordinate(pos / (R * R), pos / R % R, pos % R);
    }

    public int getFilledCound() {
        return filledCound;
    }

    public int connectedComponents(int x, int y, int z) {
        return unionFind.size(getPos(x, y, z));
    }

    public boolean get(final int x, final int y, final int z) {
        assert (0 <= Math.min(x, Math.min(y, z))) && (Math.max(x, Math.max(y, z)) < R);
        final int pos = getPos(x, y, z);
        return ((board[pos >> 3] >> (pos & 7)) & 1) == 1;
    }

    public boolean get(final Coordinate c) {
        return get(c.x, c.y, c.z);
    }

    public boolean flip(final int x, final int y, final int z, int target) {
        assert in(x) && in(y) && in(z);
        final int pos = getPos(x, y, z);
        if (((board[pos >> 3] >> (pos & 7)) & 1) == target) {
            return false;
        }
        board[pos >> 3] ^= 1 << (pos & 7);
        return true;
    }

    public boolean fill(final int x, final int y, final int z) {
        if (!flip(x, y, z, 1)) {
            return false;
        }
        filledCound++;
        uniteAdjacents(x, y, z);
        if (y == 0) {
            unionFind.unite(getPos(x, y, z), R * R * R);
        }
        return true;
    }

    private void resetConnectivityDfs(final int x, final int y, final int z, boolean reset) {
        if (reset) {
            if (unionFind.size(getPos(x, y, z)) == 1) {
                return;
            }
            unionFind.reset(getPos(x, y, z));
        } else {
            if (unionFind.size(getPos(x, y, z)) != 1) {
                return;
            }
            if ((y == 0) && get(x, y, z)) {
                unionFind.unite(getPos(x, y, z), R * R * R);
            }
        }
        for (final int[] d : Coordinate.ADJACENTS) {
            if (in(x + d[0]) && in(y + d[1]) && in(z + d[2])) {
                if (get(x + d[0], y + d[1], z + d[2])) {
//                    resetConnectivityDfs(x + d[0], y + d[1], z + d[2], reset);
                    if (!reset && get(x, y, z)) {
                        unionFind.unite(getPos(x, y, z), getPos(x + d[0], y + d[1], z + d[2]));
                    }
                }
            }
        }
    }

    public boolean doVoid(final int x, final int y, final int z) {
        if (!flip(x, y, z, 0)) {
            return false;
        }
        filledCound --;
        forceGrounded = true;
//        resetConnectivityDfs(x, y, z, true);
//        resetConnectivityDfs(x, y, z, false);
        return true;
    }

    public boolean fill(final Coordinate p) {
        return fill(p.x, p.y, p.z);
    }

    public boolean doVoid(final Coordinate p) {
        return doVoid(p.x, p.y, p.z);
    }

    public String getPath() {
        return path;
    }

    public boolean grounded() {
        return forceGrounded || (unionFind.size(R * R * R) == (filledCound + 1));
    }

    private void uniteAdjacents(final int x, final int y, final int z) {
        for (final int[] d : Coordinate.ADJACENTS) {
            if (in(x + d[0]) && in(y + d[1]) && in(z + d[2])) {
                if (get(x + d[0], y + d[1], z + d[2])) {
                    unionFind.unite(getPos(x, y, z), getPos(x + d[0], y + d[1], z + d[2]));
                }
            }
        }
    }

    public boolean in(final int x) {
        return (0 <= x) && (x < R);
    }

    public boolean in(final Coordinate p) {
        return in(p.x) && in(p.y) && in(p.z);
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
