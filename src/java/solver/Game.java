package solver;

import java.util.List;

public class Game {
    private final State state;
    private final Board goal;
    private final List<Trace> traces;
    private int finishedTraces = 0;

    public Game(final Board goal, final List<Trace> traces) {
        state = State.getInitialState(goal.getR());
        this.goal = goal;
        this.traces = traces;
    }

    public State getState() {
        return state;
    }

    public boolean proceed() {
        Validator.validateGlobal(state);
        state.consumeGlobalCost();
        if (traces.get(finishedTraces).type == Trace.Type.HALT) {
            Validator.validateHalt(state);
            return false;
        }
        final int botCount = state.getBots().size();
        state.step(traces.subList(finishedTraces, finishedTraces + botCount));
        finishedTraces += botCount;
        return true;
    }

    public boolean validateSuccess() {
        assert (traces.get(finishedTraces).type == Trace.Type.HALT) : "game not halted";
        assert ((finishedTraces + 1) == traces.size()) : "traces remains";
//        outputBoard(state.getBoard());
//        for(int i=0;i<state.getBoard().getBoard().length;i++) {
//            if (state.getBoard().getBoard()[i] != goal.getBoard()[i]) {
//                System.out.printf("different %d: %d %d\n", i, state.getBoard().getBoard()[i], goal.getBoard()[i]);
//            }
//        }
        assert state.getBoard().equals(goal) : "different board";
        return (traces.get(finishedTraces).type == Trace.Type.HALT)
                && ((finishedTraces + 1) == traces.size())
                && state.getBoard().equals(goal);
    }
}
