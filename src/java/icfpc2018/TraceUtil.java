package icfpc2018;

import com.google.common.collect.Lists;
import icfpc2018.models.Board;
import icfpc2018.models.Coordinate;
import icfpc2018.models.Trace;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TraceUtil {
    public static List<Trace> shortestPath(final Board board, final Coordinate start, final Coordinate end) {
        final Queue<Coordinate> queue = new ArrayDeque<>();
        final int R = board.getR();
        final int Rcube = R * R * R;

        final int[] memo = new int[Rcube];

        queue.add(start);

        Coordinate c;
        while ((c = queue.poll()) != null) {
            if (c.equals(end)) {
                break;
            }

            // SMOVE
            final int prevPos = board.getPos(c.x, c.y, c.z);

            for (int i = 1; i <= 3; i++) {
                for (int k = -1; k <= 1; k += 2) {
                    for (int j = 1; j <= 15; j++) {
                        final Coordinate clone = c.clone();
                        final int val = j * k;
                        clone.applyLld(i, val);
                        if (board.get(clone)) {
                            continue;
                        }
                        final int pos = board.getPos(clone.x, clone.y, clone.z);
                        if (memo[pos] < Rcube) {
                            queue.add(clone);
                            memo[pos] = i * Rcube + prevPos;
                        }
                    }
                }
            }

            // LMOVE
            for (int i = 1; i <= 3; i++) {
                for (int m = -1; m <= 1; m += 2) {
                    for (int j = 1; j <= 5; j++) {
                        final int val1 = j * m;
                        final Coordinate clone = c.clone();
                        clone.applySld(i, val1);
                        if (board.get(clone)) { // filled
                            continue;
                        }
                        for (int k = 1; k <= 3; k++) {
                            for (int n = -1; n <= 1; n += 2)
                                for (int l = 0; l <= 10; l++) {
                                    final int val2 = l * n;
                                    clone.applySld(k, val2); //
                                    if (board.get(clone)) { // filled
                                        continue;
                                    }
                                    final int pos = board.getPos(clone.x, clone.y, clone.z);
                                    if (memo[pos] < Rcube) {
                                        queue.add(clone);
                                        memo[pos] = i * Rcube + prevPos;
                                    }
                                }
                        }
                    }
                }
            }
        }

        int prevPos = board.getPos(end.x, end.y, end.z);
        final List<Trace> traces = new ArrayList<>();

        // Revive trace
        while (prevPos != board.getPos(start.x, start.y, start.z)) {
            final Coordinate cTo = board.fromPos(prevPos);
            int firstAxis = memo[prevPos] / Rcube; // 1:x, 2:y, 3:z
            prevPos = memo[prevPos] % Rcube;
            final Coordinate cFrom = board.fromPos(prevPos);

            final Coordinate d = new Coordinate(cTo.x - cFrom.x, cTo.y - cFrom.y, cTo.z - cFrom.z);
            if (d.clen() == d.mlen()) {
                traces.add(Coordinate.toSmove(d));
            } else {
                final Coordinate first = new Coordinate(
                        (firstAxis == 1) ? d.x : 0,
                        (firstAxis == 2) ? d.y : 0,
                        (firstAxis == 3) ? d.z : 0
                );
                final Coordinate second = new Coordinate(
                        (firstAxis == 1) ? 0 : d.x,
                        (firstAxis == 2) ? 0 : d.y,
                        (firstAxis == 3) ? 0 : d.z
                );
                traces.add(Coordinate.toLmove(first, second));
            }
        }

        return Lists.reverse(traces);
    }
}
