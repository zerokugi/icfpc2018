package icfpc2018;

import icfpc2018.models.Board;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author ryosukek
 */

public class DebugUtil {
    public static void outputBoard(final Board board) {
        try {
            final FileOutputStream output = new FileOutputStream("debug.mdl");

            final byte buf[] = new byte[1];
            buf[0] = (byte) board.getR();
            output.write(buf, 0, 1);
            output.write(board.getBoard(), 0, board.getBoard().length);
            output.flush();
            output.close();
        } catch (IOException e) {
        }
    }
}
