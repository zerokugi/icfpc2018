package icfpc2018.solver;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import icfpc2018.Game;
import icfpc2018.TraceExporter;
import icfpc2018.models.Board;
import icfpc2018.models.Trace;
import icfpc2018.solver.construct.BaseConstructSolver;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static icfpc2018.DebugUtil.outputBoard;

@Ignore
@RunWith(Parameterized.class)
public class BaseSolverTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static Map<String, ScoreSummary> scoreMap = new LinkedHashMap<>();
    private static Map<String, ScoreSummary> bestScoreMap = new LinkedHashMap<>();
    public BaseSolver solver;
    @Parameterized.Parameter(0) // first data value (0) is default
    public String srcPath;
    @Parameterized.Parameter(1)
    public String tgtPath;
    @Parameterized.Parameter(2)
    public String testcase;

    private static Board getState(final String path) throws IOException {
        final FileInputStream input = new FileInputStream(path);
        final byte[] Rb = new byte[1];
        assert input.read(Rb) == 1;
        final int R = Rb[0] & 0xff;
        final int readBytes = ((R * R * R) + 7) / 8;
        final byte[] board = new byte[readBytes];
        assert readBytes == input.read(board);
        input.close();
        return new Board(R, board, path);
    }

    @AfterClass
    public static void tearDown() {
        try {
            // Update by best score
            final LinkedHashMap<String, ScoreSummary> newBestScoreMap = new LinkedHashMap<>();
            for (Map.Entry<String, ScoreSummary> entry : scoreMap.entrySet()) {
                final String path = entry.getKey();
                final ScoreSummary summary = entry.getValue();
                final ScoreSummary bestScore = bestScoreMap.get(path);
                if (bestScore == null || bestScore.score.longValue() > summary.score.longValue()) {
                    newBestScoreMap.put(path, summary);
                } else {
                    newBestScoreMap.put(path, bestScore);
                }
            }
            final String scoreJson = MAPPER.writeValueAsString(newBestScoreMap);
            try (PrintStream printStream = new PrintStream(new FileOutputStream("dist/bestTraces/summary.json"))) {
                printStream.print(scoreJson);
            }
            System.out.println("Score saved.");

            System.out.println("Reading default score");

            final FileInputStream input = new FileInputStream("dist/bestTraces/default.json");
            final Map<String, ScoreSummary> defaultScore = MAPPER.readValue(input, new TypeReference<LinkedHashMap<String, ScoreSummary>>() {
            });

            // Enable when new cases run with DefaultSolver
//            scoreMap.forEach(bestScoreMap::putIfAbsent);
//            scoreMap.forEach(defaultScore::putIfAbsent);
//            final String defaultScoreJson = MAPPER.writeValueAsString(defaultScore);
//            try (PrintStream printStream = new PrintStream(new FileOutputStream("dist/bestTraces/default.json"))) {
//                printStream.print(defaultScoreJson);
//            }

            System.out.println("============ Score Summary ============");
            final List<String> updated = new ArrayList<>();

            final ColoredPrinter printer = new ColoredPrinter.Builder(1, false).build();
            long totalEnergy = 0;
            for (Map.Entry<String, ScoreSummary> entry : scoreMap.entrySet()) {
                final String path = entry.getKey();
                final ScoreSummary summary = entry.getValue();
                final Date date = new Date(summary.timestamp.longValue());
                final ScoreSummary oldSummary = bestScoreMap.get(path);
                final ScoreSummary defaultSummary = defaultScore.get(path);
                final double ratio = (1.0 * summary.score.longValue()) / defaultSummary.score.longValue();
                final double oldRatio = (1.0 * oldSummary.score.longValue()) / defaultSummary.score.longValue();
                totalEnergy += Math.max(summary.score.longValue(), oldSummary.score.longValue());

                if (ratio < 1) { // if good
                    final String s;
                    final String t;
                    if (summary.score.longValue() <= oldSummary.score.longValue()) { // best
                        printer.setForegroundColor(Ansi.FColor.GREEN);
                    }
                    if (summary.score.longValue() < oldSummary.score.longValue()) { // best
                        s = String.format(" | %s(%s) -> %s(%s)",
                                oldSummary.score, new Date(oldSummary.timestamp.longValue()),
                                summary.score, date
                        );
                        t = String.format(
                                "%4f (%4f)",
                                ratio,
                                ratio - oldRatio
                        );
                        printer.setAttribute(Ansi.Attribute.BOLD);
                        updated.add(String.format("%s: *%s* %s", path, t, s));
                    } else {
                        s = String.format(" | %s(%s)",
                            summary.score,
                            date
                        );
                        t = String.format(
                                "%4f (%4f)",
                                ratio,
                                ratio - oldRatio
                        );
                    }
                    printer.print(path + ": ");

                    printer.print(t);
                    printer.setAttribute(Ansi.Attribute.NONE);
                    printer.println(s);
                } else {
                    if (ratio > 1) { // if bad
                        printer.setForegroundColor(Ansi.FColor.RED);
                    }
                    final String s = String.format("%4f | %s(%s)",
                            ratio,
                            summary.score,
                            date
                    );
                    printer.println(path + ": " + s);
                }
                printer.clear();
            }
            printer.println("totalEnergy = " + totalEnergy + "\n");

            if (!updated.isEmpty()) {
                printer.println(updated.size() + "updated!");
                final StringBuilder sb = new StringBuilder();
                if (updated.size() == 1) {
                    sb.append(String.format("Updated 1 new trace!"));
                } else {
                    sb.append(String.format("Updated %d new traces!", updated.size()));
                }
                sb.append('\n');
                sb.append("totalEnergy = " + totalEnergy + "\n");
                for (final String message : updated) {
                    sb.append(message).append('\n');
                }
                sendSlack(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendSlack(final String message) {
        final String url = "https://hooks.slack.com/services/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";
        final RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("payload", String.format("{\"text\": \"%s\"}", message))
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            System.err.println("Failed to post message.");
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws IOException {
        final Board srcBoard = Strings.isNullOrEmpty(srcPath) ? null : getState(srcPath);
        final Board tgtBoard = Strings.isNullOrEmpty(tgtPath) ? null : getState(tgtPath);
        final List<Trace> traces = solver.solve(
                srcBoard,
                tgtBoard
        );
        assert (tgtBoard == null) || ((0 < tgtBoard.getR()) && (tgtBoard.getR() <= 250));
        assert (srcBoard == null) || ((0 < srcBoard.getR()) && (srcBoard.getR() <= 250));
        assert traces != null;
//        final String types = traces.stream().map(trace -> trace.type.name()).distinct().collect(Collectors.joining());
//        System.out.println(types);
        final Game game = new Game(
                (srcBoard == null) ? Board.getInitialBoard(tgtBoard.getR()) : srcBoard,
                (tgtBoard == null) ? Board.getInitialBoard(srcBoard.getR()) : tgtBoard,
                traces);
        while (game.proceed()) {
//            System.out.printf("%d\n", game.getState().getEnergy());
        }
        assert game.validateSuccess() : "game not success";
//        System.out.printf("%s: %d\n", goal.getPath(), game.getState().getEnergy());

        final ScoreSummary scoreSummary = new ScoreSummary(game.getState().getEnergy(), System.currentTimeMillis());
        scoreSummary.traces = traces;
        scoreMap.put(testcase, scoreSummary);
    }

    @After
    public void after() {
//        System.out.println(scoreMap.get(path).traces.size() + " commands");
//        TraceExporter.export("dist/traces", testcase, scoreMap.get(path).traces);
        if (bestScoreMap.get(testcase) == null || scoreMap.get(testcase).score.longValue() < bestScoreMap.get(testcase).score.longValue()) {
            TraceExporter.export("dist/bestTraces", testcase, scoreMap.get(testcase).traces);
        }
        scoreMap.get(testcase).traces = null;
    }

    public static class ScoreSummary {
        public Number score;
        public Number timestamp;
        @JsonIgnore
        public List<Trace> traces;

        public ScoreSummary(Number score, Number timestamp) {
            this.score = score;
            this.timestamp = timestamp;
        }

        public ScoreSummary() {
        }
    }

    static {
        try {
            final FileInputStream input = new FileInputStream("dist/bestTraces/summary.json");
            bestScoreMap = MAPPER.readValue(input, new TypeReference<LinkedHashMap<String, ScoreSummary>>() {
            });
            scoreMap = new LinkedHashMap<>(bestScoreMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
