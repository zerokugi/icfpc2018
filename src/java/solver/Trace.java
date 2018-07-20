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

    public Trace(final Type type, final Integer val0) {
        this(type, val0, null, null, null);
    }

    public Trace(final Type type, final Integer val0, final Integer val1) {
        this(type, val0, val1, null, null);
    }

    public enum Type {
        HALT,
        WAIT {
            @Override
            public void execute(final State state, final Integer bi, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {

            }
        },
        FLIP {
            @Override
            public void execute(final State state, final Integer bi, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {
//                System.out.printf("fliped\n");
                state.flip();
            }
        },
        SMOVE {
            @Override
            public void execute(final State state, final Integer bi, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {
                final Coordinate p = state.getBots().get(bi).pos;
                p.applyLld(v0, v1);
//                System.out.printf("moved to (%d, %d, %d)\n", p.x, p.y, p.z);
                state.addEnergy(2 * Math.abs(v1 - 15));
            }
        },
        LMOVE {
//            @Override
//            public void execute(final State state, final Integer bi, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {
//
//            }
        },
        FUSIONP {
//            @Override
//            public void execute(final State state, final Integer bi, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {
//
//            }
        },
        FUSIONS {
//            @Override
//            public void execute(final State state, final Integer bi, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {
//
//            }
        },
        FISSION {
//            @Override
//            public void execute(final State state, final Integer bi, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {
//
//            }
        },
        FILL {
            @Override
            public void execute(final State state, final Integer bi, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {
                final Coordinate p = state.getBots().get(bi).pos.clone();
                p.applyNld(v0);
//                System.out.printf("filled (%d, %d, %d)\n", p.x, p.y, p.z);
                if (state.getBoard().fill(p)) {
                    state.addEnergy(12);
                } else {
                    state.addEnergy(6);
                }
            }
        };

        public void execute(final State state, final Integer bi, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {
            throw new UnsupportedOperationException();
        }
    }
}
