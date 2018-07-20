package solver;

public class Instruction {

    public final Type type;
    public final Integer val0;
    public final Integer val1;

    public Instruction(final Type type, final int val0, final int val1) {
        this.type = type;
        this.val0 = val0;
        this.val1 = val1;
    }

    public Instruction(final Type type, final int val0) {
        this.type = type;
        this.val0 = val0;
        this.val1 = null;
    }

    enum Type {
        HALT,
        WAIT,
        FLIP,
        SMOVE,
        LMOVE,
        FUSIONP,
        FUSIONS,
        FISSION,
        FILL
    }
}
