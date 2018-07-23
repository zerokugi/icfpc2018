package icfpc2018;

import java.util.Arrays;

public class UnionFind {
    private final int n;
    private final int[] d;
    private final boolean[] f;

    public UnionFind(final int n) {
        this.n = n;
        d = new int[n];
        f = new boolean[n];
        Arrays.fill(d, -1);
        Arrays.fill(f, false);
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

    public void set(final int x) {
        f[root(x)] = true;
    }

    public boolean get(final int x) {
        return f[root(x)];
    }

    public void reset() {
        Arrays.fill(d, -1);
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
