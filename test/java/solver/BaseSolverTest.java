package solver;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Ignore
@RunWith(Parameterized.class)
public class BaseSolverTest {
    private static ObjectMapper MAPPER = new ObjectMapper();
    private static Map<String, ScoreSummary> scoreMap = new LinkedHashMap<>();
    private static Map<String, ScoreSummary> bestScoreMap = new LinkedHashMap<>();

    public static class ScoreSummary {
        public Number score;
        public Number timestamp;
        @JsonIgnore public List<Trace> traces;

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
            bestScoreMap = MAPPER.readValue(input, new TypeReference<LinkedHashMap<String, ScoreSummary>>(){});
            scoreMap = new LinkedHashMap<>(bestScoreMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BaseSolver solver;
    @Parameterized.Parameter // first data value (0) is default
    public String path;
    @Parameterized.Parameter(1)
    public String testcase;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return IntStream.range(1, 187)
                .mapToObj(i -> new Object[]{String.format("problemsL/LA%03d_tgt.mdl", i), String.format("LA%03d", i)})
                .collect(Collectors.toList());
    }

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

    @Test
    public void testTest1() throws IOException {
        final Board goal = getState(path);
        final List<Trace> traces = solver.solve(goal);
        assert (0 < goal.getR()) && (goal.getR() <= 250);
        assert traces != null;
//        final String types = traces.stream().map(trace -> trace.type.name()).distinct().collect(Collectors.joining());
//        System.out.println(types);
        final Game game = new Game(goal, traces);
        while (game.proceed()) {
//            System.out.printf("%d\n", game.getState().getEnergy());
        }
        assert game.validateSuccess() : "game not success";
        System.out.printf("%s: %d\n", goal.getPath(), game.getState().getEnergy());

        final ScoreSummary scoreSummary = new ScoreSummary(game.getState().getEnergy(), System.currentTimeMillis());
        final ScoreSummary bestScore = bestScoreMap.get(path);
        if (bestScore == null || bestScore.score.longValue() > scoreSummary.score.longValue()) {
            scoreMap.put(path, scoreSummary);
        }
        scoreMap.get(path).traces = traces;
    }

    @After
    public void after() {
        System.out.println(scoreMap.get(path).traces.size() + " commands");
        TraceExporter.export("dist/traces", testcase, scoreMap.get(path).traces);
        if (bestScoreMap.get(path) == null || !Objects.equals(scoreMap.get(path).timestamp, bestScoreMap.get(path).timestamp)) {
            TraceExporter.export("dist/bestTraces", testcase, scoreMap.get(path).traces);
        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            final String scoreJson = MAPPER.writeValueAsString(scoreMap);
            try (PrintStream printStream = new PrintStream(new FileOutputStream("dist/bestTraces/summary.json"))) {
                printStream.print(scoreJson);
            }
            System.out.println("Score saved.");

            System.out.println("Reading default score");
            final FileInputStream input = new FileInputStream("dist/bestTraces/default.json");
            final Map<String, ScoreSummary> defaultScore = MAPPER.readValue(input, new TypeReference<LinkedHashMap<String, ScoreSummary>>() {
            });

            System.out.println("============ Score Summary ============");
            final ColoredPrinter printer = new ColoredPrinter.Builder(1, false).build();

            for (Map.Entry<String, ScoreSummary> entry : scoreMap.entrySet()) {
                final String path = entry.getKey();
                final ScoreSummary summary = entry.getValue();
                final Date date = new Date(summary.timestamp.longValue());
                final ScoreSummary oldSummary = bestScoreMap.get(path);
                final ScoreSummary defaultSummary = defaultScore.get(path);
                final double ratio = 1.0 * summary.score.longValue() / defaultSummary.score.longValue();
                if (!Objects.equals(summary.timestamp, oldSummary.timestamp)) { // if updated
                    final String s = String.format(" | %s(%s) -> %s(%s)",
                            oldSummary.score, new Date(oldSummary.timestamp.longValue()),
                            summary.score, date
                    );
                    printer.setForegroundColor(Ansi.FColor.GREEN);
                    printer.print(path + ": ");
                    printer.setAttribute(Ansi.Attribute.BOLD);

                    printer.print(String.format("%4f (%s)",
                            ratio,
                            summary.score.longValue() - oldSummary.score.longValue())
                    );
                    printer.setAttribute(Ansi.Attribute.NONE);
                    printer.println(s);
                } else {
                    printer.clear();
                    final String s = String.format("%4f | %s(%s)",
                            ratio,
                            summary.score,
                            date
                    );
                    printer.println(path + ": " + s);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
