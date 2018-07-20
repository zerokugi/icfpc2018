package solver;

import java.util.ArrayList;
import java.util.List;

public class State {
    private final Board board;
    private final List<Bot> bots;
    private long energy;
    private Harmonics harmonics;

    public State(final Board board) {
        this.board = board;
        bots = new ArrayList<>();
        bots.add(new Bot());
        harmonics = Harmonics.LOW;
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

    private boolean validate() {
        if (harmonics == Harmonics.LOW) {
            if (!board.grounded()) {
                return false;
            }
        }

        // if (bots have same bid) {
        //   return false;
        // }

        // TODO: koko ni sonota no jouken

        return true;
    }

    public void flip() {
        harmonics = (harmonics == Harmonics.LOW) ? Harmonics.HIGH : Harmonics.LOW;
    }

    public void consumeGlobalCost() {
        energy += board.getR() * board.getR() * board.getR() * ((harmonics == Harmonics.LOW) ? 3 : 30);
        energy += bots.size() * 20;
    }

    public void step(final List<Trace> traces) {
        assert bots.size() == traces.size() : "bots and traces are inconsistent numbers.";
        for (int i = 0; i < bots.size(); i++) {
            final Trace trace = traces.get(i);
            trace.type.execute(this, i, trace.val0, trace.val1, trace.val2, trace.val3);
        }
        // TODO: arrange bots
    }

    enum Harmonics {
        LOW,
        HIGH
    }
}
