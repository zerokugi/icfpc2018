package solver.dflt;

import solver.BaseSolver;
import solver.State;
import solver.Trace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefaultSolver extends BaseSolver {

    private byte[] loadTraces(final String path) throws IOException {
        final int length = (int) new File(path).length();
        final FileInputStream input = new FileInputStream(path);
        final byte[] buf = new byte[length];
        assert input.read(buf) == length;
        return buf;
    }

    public List<Trace> convertTraces(final byte[] rawTraces) {
        final List<Trace> traces = new ArrayList<>();
        for (int i = 0; i < rawTraces.length; ) {
            final int v0 = rawTraces[i++] & 0xff;
            if (v0 == 0xff) {
                traces.add(new Trace(Trace.Type.HALT));
            } else if (v0 == 0xfe) {
                traces.add(new Trace(Trace.Type.WAIT));
            } else if (v0 == 0xfd) {
                traces.add(new Trace(Trace.Type.FLIP));
            } else if ((v0 & 7) == 7) {
                traces.add(new Trace(Trace.Type.FUSIONP, v0 >> 3));
            } else if ((v0 & 7) == 6) {
                traces.add(new Trace(Trace.Type.FUSIONS, v0 >> 3));
            } else if ((v0 & 7) == 3) {
                traces.add(new Trace(Trace.Type.FILL, v0 >> 3));
            } else {
                assert (i < rawTraces.length) : "failed to decode.";
                final int v1 = rawTraces[i++] & 0xff;
                if ((v0 & 7) == 5) {
                    traces.add(new Trace(Trace.Type.FISSION, v0 >> 3, v1));
                } else if ((v0 & 15) == 4) {
                    traces.add(new Trace(
                            Trace.Type.SMOVE,
                            v0 >> 4,
                            v1
                    ));
                } else if ((v0 & 15) == 12) {
                    traces.add(new Trace(
                            Trace.Type.LMOVE,
                            (v0 >> 4) & 3,
                            (v1 >> 0) & 15,
                            (v0 >> 6) & 3,
                            (v1 >> 4) & 15
                    ));
                } else {
                    assert false : "No trace commands are found.";
                }
            }
        }
        System.out.printf("%d traces are loaded.\n", traces.size());
        return traces;
    }

    @Override
    public List<Trace> solve(final State finalState) {
        try {
            final byte[] rawTraces = loadTraces(
                    finalState.getPath()
                            .replace("problemsL", "dfltTracesL")
                            .replace("_tgt.mdl", ".nbt")
            );
            return convertTraces(rawTraces);
        } catch (IOException e) {
            throw new RuntimeException("error", e);
        }
    }
}
