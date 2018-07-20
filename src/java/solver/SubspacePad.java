package solver;

import java.util.ArrayList;
import java.util.List;

public class SubspacePad {
    private final State state;
    private List<Bot> bots;
    private int energy;
    private Harmonics harmonics;

    enum Harmonics {
        LOW,
        HIGH
    }

    public SubspacePad(final State state) {
        this.state = state;
        bots = new ArrayList<>();
        bots.add(new Bot());
        harmonics = Harmonics.LOW;
    }

    private boolean validate() {
        if (harmonics == Harmonics.LOW) {
            if (!state.grounded()) {
                return false;
            }
        }

        // if (bots have same bid) {
        //   return false;
        // }

        return true;
    }
}
