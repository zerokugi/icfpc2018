package icfpc2018.models;

import com.google.common.base.Objects;
import com.google.common.primitives.Ints;

public class Coordinate {
    public static final int[][] ADJACENTS = {
            {0, -1, 0},
            {0, 1, 0},
            {1, 0, 0},
            {0, 0, 1},
            {-1, 0, 0},
            {0, 0, -1},
    };


    public static final int[][] ADJACENTS_NOUP = {
            {0, -1, 0},
            {1, 0, 0},
            {0, 0, 1},
            {-1, 0, 0},
            {0, 0, -1},
    };

    public static final int[][] NLD = {
            {1, 0, 0},
            {0, 1, 0},
            {0, -1, 0},
            {0, 0, 1},
            {0, 0, -1},
            {-1, 0, 0},
            {1, 1, 0},
            {1, 0, 1},
            {1, -1, 0},
            {1, 0, -1},
            {0, 1, 1},
            {0, -1, 1},
            {0, 1, -1},
            {0, -1, -1},
            {-1, 1, 0},
            {-1, 0, 1},
            {-1, -1, 0},
            {-1, 0, -1},
    };

    public int x;
    public int y;
    public int z;

    public Coordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Trace toNldTrace(final Trace.Type type, final Coordinate p, final Integer val1) {
        assert ((Math.abs(p.x) + Math.abs(p.y) + Math.abs(p.z)) <= 2);
        assert (Ints.max(Math.abs(p.x), Math.abs(p.y), Math.abs(p.z)) <= 1);
        return new Trace(type, (9 * (p.x + 1)) + (3 * (p.y + 1)) + p.z + 1, val1);
    }

    public static Trace toNldTrace(final Trace.Type type, final Coordinate p) {
        return toNldTrace(type, p, null);
    }

    public static Trace toSmove(final Coordinate p) {
        assert (Ints.max(Math.abs(p.x), Math.abs(p.y), Math.abs(p.z)) == (Math.abs(p.x) + Math.abs(p.y) + Math.abs(p.z)));
        assert ((Math.abs(p.x) + Math.abs(p.y) + Math.abs(p.z)) <= 15);
        return new Trace(
                Trace.Type.SMOVE,
                (p.x == 0) ? ((0 == p.y) ? 3 : 2) : 1,
                p.x + p.y + p.z + 15
        );
    }

    public static Trace toLmove(final Coordinate p1, final Coordinate p2) {
        assert (Ints.max(Math.abs(p1.x), Math.abs(p1.y), Math.abs(p1.z)) == (Math.abs(p1.x) + Math.abs(p1.y) + Math.abs(p1.z)));
        assert ((Math.abs(p1.x) + Math.abs(p1.y) + Math.abs(p1.z)) <= 15);
        assert (Ints.max(Math.abs(p2.x), Math.abs(p2.y), Math.abs(p2.z)) == (Math.abs(p2.x) + Math.abs(p2.y) + Math.abs(p2.z)));
        assert ((Math.abs(p2.x) + Math.abs(p2.y) + Math.abs(p2.z)) <= 15);
        return new Trace(
                Trace.Type.LMOVE,
                (p1.x == 0) ? ((p1.y == 0) ? 3 : 2) : 1,
                p1.x + p1.y + p1.z + 5,
                (p2.x == 0) ? ((p2.y == 0) ? 3 : 2) : 1,
                p2.x + p2.y + p2.z + 5
        );
    }

    public static Coordinate difference(final Coordinate s, final Coordinate t) {
        return new Coordinate(t.x - s.x, t.y - s.y, t.z - s.z);
    }

    public int mlen() {
        return Math.abs(x) + Math.abs(y) + Math.abs(z);
    }

    public int clen() {
        return Ints.max(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public void applyLld(final int a, final int i) {
        if (a == 1) {
            x += i - 15;
        }
        if (a == 2) {
            y += i - 15;
        }
        if (a == 3) {
            z += i - 15;
        }
    }

    public void applySld(final int a, final int i) {
        if (a == 1) {
            x += i - 5;
        }
        if (a == 2) {
            y += i - 5;
        }
        if (a == 3) {
            z += i - 5;
        }
    }

    public void applyNld(final int a) {
        x += (a / 9) - 1;
        y += ((a / 3) % 3) - 1;
        z += (a % 3) - 1;
    }

    public Coordinate clone() {
        return new Coordinate(x, y, z);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Coordinate that = (Coordinate) o;
        return x == that.x &&
                y == that.y &&
                z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", x, y, z);
    }
}
