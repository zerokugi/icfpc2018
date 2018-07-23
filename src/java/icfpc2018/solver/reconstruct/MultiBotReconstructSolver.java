//package icfpc2018.solver.reconstruct;
//
//import com.google.common.collect.ImmutableList;
//import com.google.common.collect.Lists;
//import icfpc2018.models.Board;
//import icfpc2018.models.Coordinate;
//import icfpc2018.models.State;
//import icfpc2018.models.Trace;
//import icfpc2018.solver.BaseSolver;
//
//import java.util.ArrayDeque;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//import java.util.Queue;
//import java.util.stream.Collectors;
//
//import static icfpc2018.TraceOptimizer.getOptimalMove;
//import static icfpc2018.TraceOptimizer.go;
//import static icfpc2018.models.Coordinate.ADJACENTS;
//import static icfpc2018.models.Coordinate.ADJACENTS_NOUP;
//import static icfpc2018.models.Coordinate.difference;
//import static icfpc2018.models.Trace.Type.FILL;
//import static icfpc2018.models.Trace.Type.FLIP;
//import static icfpc2018.models.Trace.Type.HALT;
//import static icfpc2018.models.Trace.Type.SMOVE;
//import static icfpc2018.models.Trace.Type.VOID;
//import static icfpc2018.solver.ReverseSolver.reverseTraces;
//
//public class MultiBotReconstructSolver extends BaseSolver {
//
//    private final List<Coordinate> fullVoxels = Lists.newArrayList();
//    private Board initialBoard;
//    private Board finalBoard;
//    private int R;
//    private byte[] vis;
//    private int assign[];
//    private List<Coordinate> startPoints;
//    private List<Trace> traces;
//    private State state;
//
//    private void visit(final Coordinate p) {
//        final int pos = finalBoard.getPos(p);
//        vis[pos >> 3] |= 1 << (pos & 7);
//    }
//
//    private boolean visited(final Coordinate p) {
//        final int pos = finalBoard.getPos(p);
//        return ((vis[pos >> 3] >> (pos & 7)) & 1) == 1;
//    }
//
//    private void fillAssign(final List<Coordinate> startPoints) {
//        final Queue<Integer> queue = new ArrayDeque<>();
//        for (int i = 0; i < startPoints.size(); i++) {
//            queue.add((finalBoard.getPos(startPoints.get(i)) * 40) + i);
//            assign[finalBoard.getPos(startPoints.get(i))] = i + 1;
//        }
//        while (!queue.isEmpty()) {
//            final int pos = queue.peek() / 40;
//            final int id = queue.peek() % 40;
//            final Coordinate p = finalBoard.fromPos(pos);
//            queue.poll();
//            for (final int[] d : ADJACENTS) {
//                final Coordinate q = new Coordinate(p.x + d[0], p.y + d[1], p.z + d[2]);
//                if (finalBoard.in(q)) {
//                    final int qPos = finalBoard.getPos(q);
//                    if (assign[qPos] == 0) {
//                        assign[qPos] = id + 1;
//                        queue.add(qPos * 40 + id);
//                    }
//                }
//            }
//        }
//    }
//
//    private void initializeAssign() {
//        assign = new int[R * R * R];
//        for (int x = 0; x < R; x++) {
//            for (int y = 0; y < R; y++) {
//                for (int z = 0; z < R; z++) {
//                    if (initialBoard.get(x, y, z) || finalBoard.get(x, y, z)) {
//                        assign[initialBoard.getPos(x, y, z)] = 0;
//                    } else {
//                        assign[initialBoard.getPos(x, y, z)] = -1;
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public List<Trace> solve(final Board initialBoard, final Board finalBoard) {
//        R = finalBoard.getR();
//        vis = new byte[finalBoard.getBoard().length];
//        startPoints = ImmutableList.of(
//                new Coordinate(0, 0, 0),
//                new Coordinate(R - 1, 0, 0),
//                new Coordinate(0, 0, R - 1),
//                new Coordinate(R - 1, 0, R - 1)
//        );
//        this.initialBoard = new Board(R, initialBoard.getBoard(), "");
//        this.finalBoard = new Board(R, finalBoard.getBoard(), "");
//        initializeAssign();
//        state = State.getInitialState(initialBoard);
//        final List<List<Coordinate>> nearestPoints = enumerateNearestPoints(startPoints);
//        final List<Trace> resultTraces = new ArrayList<>();
//        resultTraces.add(new Trace(FLIP));
//        for (final List<Coordinate> nearestPoint : nearestPoints) {
//            fillAssign(nearestPoint);
//            for (int i = 0; i < nearestPoint.size(); i++) {
//                resultTraces.addAll(solveNearestComponent(nearestPoint.get(i), i + 1));
//            }
//        }
//        resultTraces.add(new Trace(FLIP));
//        resultTraces.add(new Trace(HALT));
//        return resultTraces;
//    }
//
//    private List<List<Coordinate>> enumerateNearestPoints(final List<Coordinate> origins) {
//        final List<List<Coordinate>> nearestPointsList = new ArrayList<>();
//        while (true) {
//            final List<Coordinate> nearestPoints = origins.stream().map(origin -> {
//                final Coordinate nearestPoint = getNearestPoint(origin);
//                if (nearestPoint == null) {
//                    return null;
//                }
//                final Queue<Coordinate> queue = new ArrayDeque<>();
//                queue.add(nearestPoint);
//                visit(nearestPoint);
//                while (!queue.isEmpty()) {
//                    final Coordinate p = queue.poll();
//                    for (final int[] d : ADJACENTS) {
//                        final Coordinate q = new Coordinate(p.x + d[0], p.y + d[1], p.z + d[2]);
//                        if (finalBoard.in(q) && !visited(q) && (finalBoard.get(q) || initialBoard.get(q))) {
//                            visit(q);
//                            queue.add(q);
//                        }
//                    }
//                }
//                return nearestPoint;
//            }).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//            if (nearestPoints.isEmpty()) {
//                break;
//            } else {
//                nearestPointsList.add(nearestPoints);
//            }
//        }
//        Arrays.fill(vis, (byte) 0);
//        return Lists.reverse(nearestPointsList);
//    }
//
//    public List<Trace> solveNearestComponent(final Coordinate start, final int id) {
//        traces = new ArrayList<>();
//        final Coordinate nextToStart = new Coordinate(start.x - 1, start.y, start.z);
//        traces.addAll(go(new Coordinate(0, 0, 0), nextToStart));
//        dfs(start, nextToStart, id, true);
//        traces.addAll(reverseTraces(go(new Coordinate(0, 0, 0), nextToStart)));
//        if (state.getHarmonics() == State.Harmonics.HIGH) {
//            tryFlip();
//        }
//        return traces;
//    }
//
//    private void dfs(final Coordinate p, final Coordinate parent, final int id, boolean canUp) {
//        visit(p);
//        if (initialBoard.get(p)) {
//            changeFillVoid(p, parent, false);
//        }
//
//        traces.add(getOptimalMove(parent, p));
//        final List<Coordinate> candidates = new ArrayList<>();
//        for (final int[] d : canUp ? ADJACENTS : ADJACENTS_NOUP) {
//            final Coordinate q = new Coordinate(p.x + d[0], p.y + d[1], p.z + d[2]);
//            if (finalBoard.in(q) && (assign[finalBoard.getPos(q)] == id) && !visited(q)) {
//                candidates.add(q);
//                visit(q);
//            }
//        }
//
//        for (final Coordinate q : candidates) {
//            dfs(q, p, id, canUp);
//        }
//
//        if (traces.get(traces.size() - 1).type == SMOVE) {
//            traces.remove(traces.size() - 1);
//        } else {
//            traces.add(getOptimalMove(p, parent));
//        }
//        if (finalBoard.get(p)) {
//            changeFillVoid(p, parent, true);
//        }
//    }
//
//    private void changeFillVoid(final Coordinate p, final Coordinate parent, final boolean isFill) {
//        if (isFill) {
//            state.getBoard().fill(p);
//        } else {
//            state.getBoard().doVoid(p);
//        }
////        if (!state.getBoard().mustBeGrounded() && (state.getHarmonics() == State.Harmonics.LOW)) {
////            state.flipHarmonics();
////            tryFlip();
////        }
//        traces.add(Coordinate.toNldTrace(isFill ? FILL : VOID, difference(parent, p)));
////        if (state.getBoard().mustBeGrounded() && (state.getHarmonics() == State.Harmonics.HIGH)) {
////            state.flipHarmonics();
////            tryFlip();
////        }
//    }
//
//    private void tryFlip() {
//        if (traces.get(traces.size() - 1).type == FLIP) {
//            traces.remove(traces.size() - 1);
//        } else {
//            traces.add(new Trace(FLIP));
//        }
//    }
//
//    private Coordinate getNearestPoint(final Coordinate o) {
//        for (int d = 0; d <= (R * 3); d++) {
//            for (int x = 0; x < R; x++) {
//                for (int y = 0; y <= 0; y++) {
//                    final int dz = d - Math.abs(x - o.x) - Math.abs(y - o.y);
//                    if (dz < 0) {
//                        continue;
//                    }
//                    for (int s = -1; s <= 1; s += 2) {
//                        final int z = o.z + (s * dz);
//                        final Coordinate p = new Coordinate(x, y, z);
//                        if (finalBoard.in(p) && !visited(p) && (initialBoard.get(p) || finalBoard.get(p))) {
//                            return p;
//                        }
//                    }
//                }
//            }
//        }
//        return null;
//    }
//}
