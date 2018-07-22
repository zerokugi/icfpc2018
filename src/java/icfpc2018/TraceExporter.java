package icfpc2018;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.primitives.Bytes;
import icfpc2018.models.Trace;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TraceExporter {
    public static void export(final String path, final String testcase, final List<Trace> traces) {
        assert traces != null;
        try (final FileOutputStream outputStream = new FileOutputStream(path + "/" + testcase + ".nbt")) {
            outputStream.write(toBinaryData(traces));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @VisibleForTesting
    static byte[] toBinaryData(final List<Trace> traces) {
        final List<Integer> rawTraces = new ArrayList<>();
        for (final Trace trace : traces) {
            switch (trace.type) {
                case HALT:
                    rawTraces.add(0b1111_1111);
                    break;
                case WAIT:
                    rawTraces.add(0b1111_1110);
                    break;
                case FLIP:
                    rawTraces.add(0b1111_1101);
                    break;
                case SMOVE:
                    rawTraces.add((trace.val0 << 4) + 0b0100);
                    rawTraces.add(trace.val1);
                    break;
                case LMOVE:
                    rawTraces.add((trace.val0 << 6) + (trace.val2 << 4) + 0b1100);
                    rawTraces.add((trace.val1 << 4) + (trace.val3));
                    break;
                case FUSIONP:
                    rawTraces.add(((trace.val0) << 3) + 0b111);
                    break;
                case FUSIONS:
                    rawTraces.add(((trace.val0) << 3) + 0b110);
                    break;
                case FISSION:
                    rawTraces.add(((trace.val0) << 3) + 0b101);
                    rawTraces.add(trace.val1);
                    break;
                case FILL:
                    rawTraces.add((trace.val0 << 3) + 0b011);
                    break;
            }
        }
        return Bytes.toArray(rawTraces);
    }
}
