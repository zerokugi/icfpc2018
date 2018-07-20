package solver;

import java.util.List;

abstract class BaseSolver {
    BaseSolver() {

    }

    public abstract List<Instruction> solve(final int initialState);
}