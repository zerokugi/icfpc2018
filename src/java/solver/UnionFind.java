package solver;

import java.util.Arrays;

public class UnionFind {
    private final int n;
    private final int[] d;

    public UnionFind(final int n) {
        this.n = n;
        d = new int[n];
        Arrays.fill(d, -1);
    }

    public boolean unite(int x, int y) {
        if ((x = root(x)) != (y = root(y))) {
            if (d[y] < d[x]) {
                return unite(y, x);
            }
            d[x] += d[y];
            d[y] = x;
        }
        return x != y;
    }

    public boolean find(final int x, final int y) {
        return root(x) == root(y);
    }

    public int root(int x) {
        return (d[x] < 0) ? x : (d[x] = root(d[x]));
    }

    public int size(int x) {
        return -d[root(x)];
    }
}
