package icfpc2018.solver.construct;

import com.google.common.collect.Lists;
import icfpc2018.models.Board;
import icfpc2018.models.Coordinate;
import icfpc2018.models.State;
import icfpc2018.models.Trace;

import java.util.ArrayList;
import java.util.List;

import static icfpc2018.TraceOptimizer.getOptimalMove;
import static icfpc2018.TraceOptimizer.go;
import static icfpc2018.models.Coordinate.ADJACENTS;
import static icfpc2018.models.Coordinate.ADJACENTS_NOUP;
import static icfpc2018.models.Coordinate.difference;
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

    private void visit(final Coordinate p) {
        final int pos = finalBoard.getPos(p);
        vis[pos >> 3] |= 1 << (pos & 7);
    }

    private boolean visited(final Coordinate p) {
        final int pos = finalBoard.getPos(p);
        return ((vis[pos >> 3] >> (pos & 7)) & 1) == 1;
    }

    @Override
    public List<Trace> solve(final Board finalBoard) {
        R = finalBoard.getR();
        vis = new byte[finalBoard.getBoard().length];
        canUps = new boolean[R * R * R];
        this.finalBoard = finalBoard;
        state = State.getInitialState(R);
        List<Trace> resultTraces = new ArrayList<>();
        while(true) {
            final List<Trace> traces = solveNearestComponent(new Coordinate(0, 0, 0));
            if (traces.isEmpty()) {
                break;
            }
            traces.addAll(resultTraces);
            resultTraces = traces;
        }
        resultTraces.add(new Trace(HALT));
        return resultTraces;
    }

    public List<Trace> solveNearestComponent(final Coordinate origin) {
        final Coordinate start = getNearestPoint();
        if (start == null) {
            return new ArrayList<>();
        }
        final Coordinate nextToStart = new Coordinate(start.x - 1, start.y, start.z - 1);
        traces = new ArrayList<>();
        traces.addAll(go(new Coordinate(0, 0, 0), nextToStart));
        dfs(start, nextToStart, true);
        traces.addAll(go(nextToStart, new Coordinate(0, 0, 0)));
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
        if (!state.getBoard().mustBeGrounded() && (state.getHarmonics() == State.Harmonics.LOW)) {
            state.flipHarmonics();
            tryFlip();
        }
        traces.add(Coordinate.toNldTrace(FILL, difference(parent, p)));
        if (state.getBoard().mustBeGrounded() && (state.getHarmonics() == State.Harmonics.HIGH)) {
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

    private Coordinate getNearestPoint() {
        for (int d = 0; d <= R * 3; d++) {
            for (int x = 0; x < R; x++) {
                for (int y = 0; y < R; y++) {
                    int z = d - x - y;
                    final Coordinate p = new Coordinate(x, y, z);
                    if (finalBoard.in(p) && !visited(p) && finalBoard.get(p)) {
                        return p;
                    }
                }
            }
        }
        return null;
    }
}
