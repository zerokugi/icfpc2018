package icfpc2018;

import com.google.common.collect.Lists;
import icfpc2018.models.Board;
import icfpc2018.models.Coordinate;
import icfpc2018.models.Trace;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static icfpc2018.models.Coordinate.toLmove;
import static icfpc2018.models.Coordinate.toSmove;

public class TraceOptimizer {
    final int R;
    final long[] memo;
    int count = 0;

    public TraceOptimizer(final int r) {
        R = r;
        memo = new long[R * R * R];
    }

    public List<Trace> shortestPath(final Board board, final Coordinate start, final Coordinate end) {
        if (start.equals(end)) {
            return new ArrayList<>();
        }
        final Queue<Integer> queue = new ArrayDeque<>();
        final int R = board.getR();
        final int Rcube = R * R * R;
        final int Rcube2 = R * R * R * 2;
        count ++;
        final long bias = ((long)count) * 4 * Rcube2;

        final int startPos = board.getPos(start);
        final int endPos = board.getPos(end);
        queue.add(startPos);
        queue.add(endPos + Rcube);
        memo[startPos] = bias + startPos;
        memo[endPos] = bias + Rcube + endPos;

        int intermediatePos = -1;
        long lastMemoValue = -1;

        bfs: while (!queue.isEmpty()) {
            final boolean isFromEnd = (queue.peek() / Rcube) == 1;
            final int prevPos = queue.poll() % Rcube;

            // SMOVE

            for (int i = 1; i <= 3; i++) {
                for (int k = -1; k <= 1; k += 2) {
                    for (int j = 1; j <= 15; j++) {
                        final Coordinate clone = board.fromPos(prevPos);
                        final int val = (j * k) + 15;
                        clone.applyLld(i, val);
                        if (!board.in(clone) || board.get(clone)) {
                            break;
                        }
                        final int pos = board.getPos(clone);
                        if (memo[pos] < bias) {
                            queue.add(pos + (isFromEnd ? Rcube : 0));
                            memo[pos] = bias + (i * Rcube2) + prevPos + (isFromEnd ? Rcube : 0);
                        } else {
                            final boolean isFromEnd2 = Rcube <= ((memo[pos] - bias) % Rcube2);
                            if (isFromEnd != isFromEnd2) {
                                intermediatePos = pos;
                                lastMemoValue = bias + (i * Rcube2) + prevPos + (isFromEnd ? Rcube : 0);
                                break bfs;
                            }
                        }
                    }
                }
            }

            // LMOVE
            for (int i = 1; i <= 3; i++) {
                for (int m = -1; m <= 1; m += 2) {
                    for (int j = 1; j <= 5; j++) {
                        final int val1 = (j * m) + 5;
                        final Coordinate clone1 = board.fromPos(prevPos).clone();
                        clone1.applySld(i, val1);
                        if (!board.in(clone1) || board.get(clone1)) { // filled
                            break;
                        }
                        for (int k = 1; k <= 3; k++) {
                            for (int n = -1; n <= 1; n += 2) {
                                for (int l = 1; l <= 5; l++) {
                                    final int val2 = (l * n) + 5;
                                    final Coordinate clone2 = clone1.clone();
                                    clone2.applySld(k, val2); //
                                    if (!board.in(clone2) || board.get(clone2)) { // filled
                                        break;
                                    }
                                    final int pos = board.getPos(clone2);
                                    if (memo[pos] < bias) {
                                        queue.add(pos + (isFromEnd ? Rcube : 0));
                                        memo[pos] = bias + (i * Rcube2) + prevPos + (isFromEnd ? Rcube : 0);
                                    } else {
                                        final boolean isFromEnd2 = Rcube <= ((memo[pos] - bias) % Rcube2);
                                        if (isFromEnd != isFromEnd2) {
                                            intermediatePos = pos;
                                            lastMemoValue = bias + (i * Rcube2) + prevPos + (isFromEnd ? Rcube : 0);
                                            break bfs;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        assert (intermediatePos != -1) && (lastMemoValue != -1)
                : "No way...";
        final boolean intermediateMemoByStart = Rcube <= ((lastMemoValue - bias) % Rcube2);
        List<Trace> traces = new ArrayList<>();
        {
            int prevPos = intermediatePos;

            // Revive trace
            while (prevPos != board.getPos(start)) {
                final Coordinate cTo = board.fromPos(prevPos);
                final long memoValue;
                if ((prevPos == intermediatePos) && !intermediateMemoByStart) {
                    memoValue = lastMemoValue - bias;
                } else {
                    memoValue = memo[prevPos] - bias;
                }
                final int firstAxis = (int) memoValue / Rcube2; // 1:x, 2:y, 3:z
                prevPos = (int) memoValue % Rcube;
                final Coordinate cFrom = board.fromPos(prevPos);

                final Coordinate d = Coordinate.difference(cFrom, cTo);
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
        }
        traces = Lists.reverse(traces);
        {
            int prevPos = intermediatePos;

            // Revive trace
            while (prevPos != board.getPos(end)) {
                final Coordinate cFrom = board.fromPos(prevPos);
                final long memoValue;
                if ((prevPos == intermediatePos) && intermediateMemoByStart) {
                    memoValue = lastMemoValue - bias;
                } else {
                    memoValue = memo[prevPos] - bias;
                }
                final int secondAxis = (int)memoValue / Rcube2; // 1:x, 2:y, 3:z
                prevPos = (int)memoValue % Rcube;
                final Coordinate cTo = board.fromPos(prevPos);

                final Coordinate d = Coordinate.difference(cFrom, cTo);
                if (d.clen() == d.mlen()) {
                    traces.add(Coordinate.toSmove(d));
                } else {
                    final Coordinate first = new Coordinate(
                            (secondAxis == 1) ? 0 : d.x,
                            (secondAxis == 2) ? 0 : d.y,
                            (secondAxis == 3) ? 0 : d.z
                    );
                    final Coordinate second = new Coordinate(
                            (secondAxis == 1) ? d.x : 0,
                            (secondAxis == 2) ? d.y : 0,
                            (secondAxis == 3) ? d.z : 0
                    );
                    traces.add(Coordinate.toLmove(first, second));
                }
            }
        }
        return traces;
    }

    public static Trace getOptimalMove(final Coordinate s, final Coordinate t) {
        final Coordinate d = new Coordinate(t.x - s.x, t.y - s.y, t.z - s.z);
        if (d.clen() == d.mlen()) {
            return toSmove(d);
        }
        return toLmove(new Coordinate(d.x, (d.x == 0) ? d.y : 0, 0), new Coordinate(0, (d.z == 0) ? d.y : 0, d.z));
    }

    public static List<Trace> go(final Coordinate s, final Coordinate t) {
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
}
