package solver;

import java.util.ArrayList;
import java.util.List;

public class State {
    private final Board board;
    private List<Bot> bots;
    private int energy;
    private Harmonics harmonics;

    enum Harmonics {
        LOW,
        HIGH
    }

    public State(final Board board) {
        this.board = board;
        bots = new ArrayList<>();
        bots.add(new Bot());
        harmonics = Harmonics.LOW;
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

    public void step(final List<Trace> traces) {
        for (final Bot bot : bots) {
            final Trace trace = traces.get(0);
            switch (trace.type) {
                case HALT:

                    break;
                case FILL:

                    break;
            }
        }
    }
}
