package solver;

public class Trace {

    public final Type type;
    public final Integer val0;
    public final Integer val1;
    public final Integer val2;
    public final Integer val3;

    public Trace(
            final Type type,
            final Integer val0,
            final Integer val1,
            final Integer val2,
            final Integer val3
    ) {
        this.type = type;
        this.val0 = val0;
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
    }

    public Trace(final Type type) {
        this(type, null, null, null, null);
    }

    public Trace(final Type type, final int val0) {
        this(type, val0, null, null, null);
    }

    public Trace(final Type type, final int val0, final int val1) {
        this(type, val0, val1, null, null);
    }

    public enum Type {
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
