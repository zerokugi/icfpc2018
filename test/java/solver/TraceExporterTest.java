package solver;

import org.junit.Test;
import solver.dflt.DefaultSolver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static solver.TraceExporter.toBinaryData;

public class TraceExporterTest {

    @Test
    public void testToBinaryData() throws IOException {
        final DefaultSolver solver = new DefaultSolver();

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
