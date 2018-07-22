package icfpc2018.models;

import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class State {
    private final Board board;
    private final List<Bot> bots;
    private long energy;
    private Harmonics harmonics;

    public State(
            final Board board,
            final List<Bot> bots,
            final long energy,
            final Harmonics harmonics
    ) {
        this.board = board;
        this.bots = bots;
        this.energy = energy;
        this.harmonics = harmonics;
    }

    public static State getInitialState(final int R) {
        return new State(
                Board.getInitialBoard(R),
                Lists.newArrayList(new Bot(
                        1,
                        new Coordinate(0, 0, 0),
                        IntStream.rangeClosed(2, 20).boxed().collect(Collectors.toList())
                )),
                0,
                Harmonics.LOW
        );
    }

    public long getEnergy() {
        return energy;
    }

    public void addEnergy(final long e) {
        energy += e;
    }

    public Board getBoard() {
        return board;
    }

    public List<Bot> getBots() {
        return bots;
    }

    public Harmonics getHarmonics() {
        return harmonics;
    }

    public void flipHarmonics() {
        harmonics = (harmonics == Harmonics.LOW) ? Harmonics.HIGH : Harmonics.LOW;
    }

    public void consumeGlobalCost() {
        energy += board.getR() * board.getR() * board.getR() * ((harmonics == Harmonics.LOW) ? 3 : 30);
        energy += bots.size() * 20;
    }

    public Optional<Bot> getBotByPos(final Coordinate pos) {
        return bots.stream().filter(bot -> pos.equals(bot.pos)).findAny();
    }

    public void step(final List<Trace> traces) {
        assert bots.size() == traces.size() : "bots and traces are inconsistent numbers.";
        for (int i = 0; i < traces.size(); i++) {
            final Trace trace = traces.get(i);
            bots.get(i).assignTrace(trace);
        }

        for (final Bot bot : bots) {
            if (bot.getAssignedTrace() != null) {
                bot.getAssignedTrace().type.execute(this, bot, bot.getAssignedTrace(), true);
//            System.out.printf("validate %s\n", bot.getAssignedTrace().type.name());
            }
        }
        for (int i = 0; i < traces.size(); i++) {
            final Trace trace = traces.get(i);
            trace.type.execute(this, bots.get(i), trace, false);
        }
        for (int i = traces.size() - 1; i >= 0; i--) {
            if (bots.get(i).getAssignedTrace().type == Trace.Type.FUSIONS) {
                bots.remove(i);
            }
        }
        bots.sort(Comparator.comparingInt(o -> o.bid));
    }

    public enum Harmonics {
        LOW,
        HIGH
    }
}
