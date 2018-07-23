package icfpc2018.solver;

import icfpc2018.TraceOptimizer;
import icfpc2018.models.Board;
import icfpc2018.models.Coordinate;
import icfpc2018.models.Trace;

import java.util.ArrayList;
import java.util.List;

import static icfpc2018.models.Trace.Type.FILL;
import static icfpc2018.models.Trace.Type.VOID;

public class MoveOptimizedSolver<Solver extends BaseSolver> extends BaseSolver {
    final Solver solver;

    public MoveOptimizedSolver(final Solver constructSolver) {
        solver = constructSolver;
    }

    private static boolean isOptimized(final int traceNum, final Coordinate diff) {
        final int x = Math.abs(diff.x);
        final int y = Math.abs(diff.y);
        final int z = Math.abs(diff.z);
        final int lmoveCount = (x / 15) + (y / 15) + (z / 15)
                + (((x % 15) > 5) ? 1 : 0) + (((y % 15) > 5) ? 1 : 0) + (((z % 15) > 5) ? 1 : 0);
        final int smoveCount = ((((0 < (x % 15)) && ((x % 15) < 6)) ? 1 : 0))
                + ((((0 < (y % 15)) && ((y % 15) < 6)) ? 1 : 0))
                + ((((0 < (z % 15)) && ((z % 15) < 6)) ? 1 : 0));
        return (lmoveCount + ((smoveCount + 1) / 2)) >= traceNum;
    }

    public static List<Trace> optimizeMove(final List<Trace> originalTraces, final Board initialBoard) {
        final Board board = initialBoard.clone();
        final List<Trace> optimizedTraces = new ArrayList<>();
        Coordinate prev = new Coordinate(0, 0, 0);
        final Coordinate current = new Coordinate(0, 0, 0);
        final TraceOptimizer traceOptimizer = new TraceOptimizer(board.getR());
        final List<Trace> moveTraces = new ArrayList<>();
        for (final Trace trace : originalTraces) {
            switch (trace.type) {
                case SMOVE:
                    moveTraces.add(trace);
                    current.applyLld(trace.val0, trace.val1);
                    break;
                case LMOVE:
                    moveTraces.add(trace);
                    current.applySld(trace.val0, trace.val1);
                    current.applySld(trace.val2, trace.val3);
                    break;
                default:
                    if (isOptimized(moveTraces.size(), Coordinate.difference(prev, current))) {
                        optimizedTraces.addAll(moveTraces);
                    } else {
                        optimizedTraces.addAll(traceOptimizer.shortestPath(board, prev, current));
                    }
                    moveTraces.clear();
                    optimizedTraces.add(trace);
                    prev = current.clone();
                    break;
            }
            if (trace.type == FILL || trace.type == VOID) {
                final Coordinate p = current.clone();
                p.applyNld(trace.val0);
                if (trace.type == FILL) {
                    board.fill(p);
                } else {
                    board.doVoid(p);
                }
            }
        }
        return optimizedTraces;
    }

    @Override
    public List<Trace> solve(final Board initialBoard, final Board finalBoard) {
        return optimizeMove(solver.solve(initialBoard.clone(), finalBoard.clone()), initialBoard);
    }
}
