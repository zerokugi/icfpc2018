package solver;

public class Coordinate {
    public static final int[][] ADJACENTS = {
            {1, 0, 0},
            {-1, 0, 0},
            {0, 1, 0},
            {0, -1, 0},
            {0, 0, 1},
            {0, 0, -1},
    };

    public int x;
    public int y;
    public int z;

    public Coordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public void applyNld(final int a) {
        x += (a / 9) - 1;
        y += ((a / 3) % 3) - 1;
        z += (a % 3) - 1;
    }

    public Coordinate clone() {
        return new Coordinate(x, y, z);
    }
}
