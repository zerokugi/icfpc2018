package icfpc2018.solver.reconstruct;

import com.google.common.collect.Lists;
import icfpc2018.models.Board;
import icfpc2018.models.Coordinate;
import icfpc2018.models.State;
import icfpc2018.models.Trace;
import icfpc2018.solver.BaseSolver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import static icfpc2018.TraceOptimizer.getOptimalMove;
import static icfpc2018.TraceOptimizer.go;
import static icfpc2018.models.Coordinate.ADJACENTS;
import static icfpc2018.models.Coordinate.ADJACENTS_NOUP;
import static icfpc2018.models.Coordinate.difference;
import static icfpc2018.models.Trace.Type.FILL;
import static icfpc2018.models.Trace.Type.FLIP;
import static icfpc2018.models.Trace.Type.HALT;
import static icfpc2018.models.Trace.Type.VOID;

public class SingleBotReconstructSolver extends BaseSolver {

    private final List<Coordinate> fullVoxels = Lists.newArrayList();
    private Board initialBoard;
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
    public List<Trace> solve(final Board initialBoard, final Board finalBoard) {
        R = finalBoard.getR();
        vis = new byte[finalBoard.getBoard().length];
        canUps = new boolean[R * R * R];
        this.initialBoard = new Board(R, initialBoard.getBoard(), "");
        this.finalBoard = new Board(R, finalBoard.getBoard(), "");
        state = State.getInitialState(initialBoard);
        final List<Coordinate> nearestPoints = enumerateNearestPoints(new Coordinate(0, 0, 0));
        traces = new ArrayList<>();
        for (final Coordinate nearestPoint : nearestPoints) {
            solveNearestComponent(nearestPoint);
        }
        traces.add(new Trace(HALT));
        return traces;
    }

    private List<Coordinate> enumerateNearestPoints(final Coordinate origin) {
        final List<Coordinate> nearestPoints = new ArrayList<>();
        while (true) {
            final Coordinate nearestPoint = getNearestPoint(origin);
            if (nearestPoint == null) {
                break;
            }
            final Queue<Coordinate> queue = new ArrayDeque<>();
            queue.add(nearestPoint);
            visit(nearestPoint);
            while (!queue.isEmpty()) {
                final Coordinate p = queue.poll();
                for (final int[] d : ADJACENTS) {
                    final Coordinate q = new Coordinate(p.x + d[0], p.y + d[1], p.z + d[2]);
                    if (finalBoard.in(q) && !visited(q) && (finalBoard.get(q) || initialBoard.get(q))) {
                        visit(q);
                        queue.add(q);
                    }
                }
            }
            nearestPoints.add(nearestPoint);
        }
        Arrays.fill(vis, (byte) 0);
        return Lists.reverse(nearestPoints);
    }

    public void solveNearestComponent(final Coordinate start) {
        if (start == null) {
            return;
        }
        final Coordinate nextToStart = new Coordinate(start.x - 1, start.y, start.z);
        traces.addAll(go(new Coordinate(0, 0, 0), nextToStart));
        dfs(start, nextToStart, true);
        traces.addAll(go(nextToStart, new Coordinate(0, 0, 0)));
        if (state.getHarmonics() == State.Harmonics.HIGH) {
            tryFlip();
        }
    }

    private void dfs(final Coordinate p, final Coordinate parent, boolean canUp) {
        visit(p);
        if (initialBoard.get(p)) {
            changeFillVoid(p, parent, false);
        }

        traces.add(getOptimalMove(parent, p));
        final List<Coordinate> candidates = new ArrayList<>();
        for (final int[] d : canUp ? ADJACENTS : ADJACENTS_NOUP) {
            final Coordinate q = new Coordinate(p.x + d[0], p.y + d[1], p.z + d[2]);
            if (finalBoard.in(q) && !visited(q) && !q.equals(parent) && (finalBoard.get(q) || initialBoard.get(q))) {
                candidates.add(q);
                visit(q);
            }
        }

        for (final Coordinate q : candidates) {
            dfs(q, p, canUp);
        }

        traces.add(getOptimalMove(p, parent));
        if (finalBoard.get(p)) {
            changeFillVoid(p, parent, true);
        }
    }

    private void changeFillVoid(final Coordinate p, final Coordinate parent, final boolean isFill) {
        if (isFill) {
            state.getBoard().fill(p);
        } else {
            state.getBoard().doVoid(p);
        }
        if (!state.getBoard().mustBeGrounded() && (state.getHarmonics() == State.Harmonics.LOW)) {
            state.flipHarmonics();
            tryFlip();
        }
        traces.add(Coordinate.toNldTrace(isFill ? FILL : VOID, difference(parent, p)));
        if (state.getBoard().mustBeGrounded() && (state.getHarmonics() == State.Harmonics.HIGH)) {
            state.flipHarmonics();
            tryFlip();
        }
    }

    private void tryFlip() {
        if (traces.get(traces.size() - 1).type == FLIP) {
            traces.remove(traces.size() - 1);
        } else {
            traces.add(new Trace(FLIP));
        }
    }

    private Coordinate getNearestPoint(final Coordinate o) {
        for (int d = 0; d <= (R * 3); d++) {
            for (int x = 0; x < R; x++) {
                for (int y = 0; y < R; y++) {
                    final int dz = d - Math.abs(x - o.x) - Math.abs(y - o.y);
                    if (dz < 0) {
                        continue;
                    }
                    for (int s = -1; s <= 1; s += 2) {
                        final int z = o.z + (s * dz);
                        final Coordinate p = new Coordinate(x, y, z);
                        if (finalBoard.in(p) && !visited(p) && (initialBoard.get(p) || finalBoard.get(p))) {
                            return p;
                        }
                    }
                }
            }
        }
        return null;
    }
}
