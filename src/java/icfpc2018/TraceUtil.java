package icfpc2018;

import icfpc2018.models.Board;
import icfpc2018.models.Coordinate;
import icfpc2018.models.Trace;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class TraceUtil {
    public static List<Trace> shortestPath(final Board board, final Coordinate start, final Coordinate end) {
        final Queue<Coordinate> queue = new ArrayDeque<>();
        final int R = board.getR();
        final Coordinate[] memo = new Coordinate[R * R * R];

        queue.add(start);

        while(queue.peek() != null) {
            Coordinate poll = queue.poll();
        }

        return null;
    }
}
