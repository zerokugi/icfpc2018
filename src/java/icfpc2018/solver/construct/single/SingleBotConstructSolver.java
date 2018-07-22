package icfpc2018.solver.construct.single;

import com.google.common.collect.Lists;
import icfpc2018.models.Board;
import icfpc2018.models.Coordinate;
import icfpc2018.models.State;
import icfpc2018.models.Trace;
import icfpc2018.solver.construct.BaseConstructSolver;

import java.util.ArrayList;
import java.util.List;

import static icfpc2018.models.Coordinate.ADJACENTS;
import static icfpc2018.models.Coordinate.ADJACENTS_NOUP;
import static icfpc2018.models.Coordinate.difference;
import static icfpc2018.models.Coordinate.toLmove;
import static icfpc2018.models.Coordinate.toSmove;
import static icfpc2018.models.Trace.Type.FILL;
import static icfpc2018.models.Trace.Type.FLIP;
import static icfpc2018.models.Trace.Type.HALT;
import static icfpc2018.models.Trace.Type.LMOVE;
import static icfpc2018.models.Trace.Type.SMOVE;

public class SingleBotConstructSolver extends BaseConstructSolver {

    private final List<Coordinate> fullVoxels = Lists.newArrayList();
    boolean isFirstFill = true;
    private Board finalBoard;
    private int R;
    private byte[] vis;
    private boolean[] canUps;
    private List<Trace> traces;
    private State state;

    public static Trace getOptimalMove(final Coordinate s, final Coordinate t) {
        final Coordinate d = new Coordinate(t.x - s.x, t.y - s.y, t.z - s.z);
        if (d.clen() == d.mlen()) {
            return toSmove(d);
        }
        return toLmove(new Coordinate(d.x, (d.x == 0) ? d.y : 0, 0), new Coordinate(0, (d.z == 0) ? d.y : 0, d.z));
    }

    private void visit(final Coordinate p) {
        final int pos = finalBoard.getPos(p.x, p.y, p.z);
        vis[pos >> 3] |= 1 << (pos & 7);
    }

    private boolean visited(final Coordinate p) {
        final int pos = finalBoard.getPos(p.x, p.y, p.z);
        return ((vis[pos >> 3] >> (pos & 7)) & 1) == 1;
    }

    @Override
    public List<Trace> solve(final Board finalBoard) {
        traces = new ArrayList<>();
        R = finalBoard.getR();
        vis = new byte[finalBoard.getBoard().length];
        canUps = new boolean[R * R * R];
        this.finalBoard = finalBoard;
        state = State.getInitialState(R);

        final Coordinate start = getNearestPoint();
        final Coordinate nextToStart = new Coordinate(start.x - 1, start.y, start.z - 1);

        traces.addAll(go(new Coordinate(0, 0, 0), nextToStart));
        dfs(start, nextToStart, true);
        traces.addAll(go(nextToStart, new Coordinate(0, 0, 0)));
        traces.add(new Trace(HALT));
        return traces;
    }

    private void dfs(final Coordinate p, final Coordinate parent, boolean canUp) {
        visit(p);
        traces.add(getOptimalMove(parent, p));
        final List<Coordinate> candidates = new ArrayList<>();
        for (final int[] d : canUp ? ADJACENTS : ADJACENTS_NOUP) {
            final Coordinate q = new Coordinate(p.x + d[0], p.y + d[1], p.z + d[2]);
            if (finalBoard.in(q) && !visited(q) && finalBoard.get(q) && !q.equals(parent)) {
                candidates.add(q);
                visit(q);
            }
        }

        for (final Coordinate q : candidates) {
            dfs(q, p, canUp);
        }
        if (isFirstFill) {
            isFirstFill = false;
            traces.clear();
            traces.addAll(go(new Coordinate(0, 0, 0), parent));
        } else {
            if ((traces.get(traces.size() - 1).type == SMOVE) || (traces.get(traces.size() - 1).type == LMOVE)) {
                traces.remove(traces.size() - 1);
            } else {
                traces.add(getOptimalMove(p, parent));
            }
        }
        state.getBoard().fill(p);
        if (!state.getBoard().grounded() && (state.getHarmonics() == State.Harmonics.LOW)) {
            state.flipHarmonics();
            tryFlip();
        }
        traces.add(Coordinate.toNldTrace(FILL, difference(parent, p)));
        if (state.getBoard().grounded() && (state.getHarmonics() == State.Harmonics.HIGH)) {
            state.flipHarmonics();
            tryFlip();
        }
    }

    void tryFlip() {
        if (traces.get(traces.size() - 1).type == FLIP) {
            traces.remove(traces.size() - 1);
        } else {
            traces.add(new Trace(FLIP));
        }
    }

    private List<Trace> go(final Coordinate s, final Coordinate t) {
        final Coordinate d = Coordinate.difference(s, t);
        final List<Trace> traces = new ArrayList<>();
        while (Math.abs(d.x) > 5) {
            final int len = Math.max(-15, Math.min(d.x, 15));
            traces.add(Coordinate.toSmove(new Coordinate(len, 0, 0)));
            d.x -= len;
        }
        while (Math.abs(d.y) > 5) {
            final int len = Math.max(-15, Math.min(d.y, 15));
            traces.add(Coordinate.toSmove(new Coordinate(0, len, 0)));
            d.y -= len;
        }
        while (Math.abs(d.z) > 5) {
            final int len = Math.max(-15, Math.min(d.z, 15));
            traces.add(Coordinate.toSmove(new Coordinate(0, 0, len)));
            d.z -= len;
        }
        if (d.clen() > 0) {
            if ((d.x != 0) && (d.y != 0) && (d.z != 0)) {
                traces.add(getOptimalMove(new Coordinate(0, 0, 0), new Coordinate(d.x, d.y, 0)));
                traces.add(getOptimalMove(new Coordinate(0, 0, 0), new Coordinate(0, 0, d.z)));
            } else {
                traces.add(getOptimalMove(new Coordinate(0, 0, 0), new Coordinate(d.x, d.y, d.z)));
            }
        }
        return traces;
    }

    private Coordinate getNearestPoint() {
        for (int d = 0; d <= R * 3; d++) {
            for (int x = 0; x < R; x++) {
                for (int y = 0; y < R; y++) {
                    int z = d - x - y;
                    final Coordinate p = new Coordinate(x, y, z);
                    if (finalBoard.in(p) && finalBoard.get(p)) {
                        return p;
                    }
                }
            }
        }
        assert false : "no fulls";
        return null;
    }
}
