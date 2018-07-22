package icfpc2018.solver.construct.single;

import icfpc2018.TraceOptimizer;
import icfpc2018.models.Board;
import icfpc2018.models.Coordinate;
import icfpc2018.models.Trace;
import icfpc2018.solver.construct.BaseConstructSolver;

import java.util.ArrayList;
import java.util.List;

import static icfpc2018.models.Trace.Type.FILL;
import static icfpc2018.models.Trace.Type.VOID;

public class MoveOptimizedConstructSolver<ConstructSolver extends BaseConstructSolver> extends BaseConstructSolver {
    final ConstructSolver constructSolver;

    public MoveOptimizedConstructSolver(final ConstructSolver constructSolver) {
        this.constructSolver = constructSolver;
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
                    if (moveTraces.size() > 1) {
                        optimizedTraces.addAll(traceOptimizer.shortestPath(board, prev, current));
                    } else {
                        optimizedTraces.addAll(moveTraces);
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
    public List<Trace> solve(final Board finalBoard) {
        return optimizeMove(constructSolver.solve(finalBoard), Board.getInitialBoard(finalBoard.getR()));
    }
}
