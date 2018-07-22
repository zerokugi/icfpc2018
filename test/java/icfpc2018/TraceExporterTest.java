package icfpc2018;

import org.junit.Test;
import icfpc2018.solver.construct.dflt.DefaultConstructSolver;
import icfpc2018.models.Board;
import icfpc2018.models.Trace;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static icfpc2018.TraceExporter.toBinaryData;
import static org.junit.Assert.*;

public class TraceExporterTest {

    @Test
    public void testToBinaryData() throws IOException {
        final DefaultConstructSolver solver = new DefaultConstructSolver();

        final String tracePath = "dfltTracesL/LA001.nbt";
        final String modelPath = "problemsL/LA001_tgt.mdl";
        int R;
        byte[] rawBoard;
        Board board;
        List<Trace> solved;
        try (FileInputStream input = new FileInputStream(modelPath)) {
            final byte[] Rb = new byte[1];
            assert input.read(Rb) == 1;
            R = Rb[0] & 0xff;
            final int readBytes = ((R * R * R) + 7) / 8;
            rawBoard = new byte[readBytes];
            assert readBytes == input.read(rawBoard);
            input.close();
        }
        board = new Board(R, rawBoard, modelPath);
        solved = solver.solve(board);

        byte[] expected = solver.loadTraces(tracePath);

        final byte[] actual = toBinaryData(solved);
        assertArrayEquals(expected, actual);
    }
}
