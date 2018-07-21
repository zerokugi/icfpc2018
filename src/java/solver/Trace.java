package solver;

import java.util.Optional;

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
            public void execute(final State state, final Bot targetBot, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {

            }
        },
        FLIP {
            @Override
            public void execute(final State state, final Bot targetBot, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {
//                System.out.printf("fliped\n");
                state.flip();
            }
        },
        SMOVE {
            @Override
            public void execute(final State state, final Bot targetBot, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {
                targetBot.pos.applyLld(v0, v1);
//                System.out.printf("moved to (%d, %d, %d)\n", targetBot.pos.x, targetBot.pos.y, targetBot.pos.z);
                state.addEnergy(2 * Math.abs(v1 - 15));
            }
        },
        LMOVE {
            @Override
            public void execute(final State state, final Bot targetBot, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {
                targetBot.pos.applySld(v0, v1);
                targetBot.pos.applySld(v2, v3);
//                System.out.printf("moved to (%d, %d, %d)\n", targetBot.pos.x, targetBot.pos.y, targetBot.pos.z);
                state.addEnergy(2 * (Math.abs(v1 - 5) + 2 + Math.abs(v3 - 5)));
            }
        },
        FUSIONP {
            @Override
            public void execute(final State state, final Bot targetBot, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {
                final Coordinate p = targetBot.pos.clone();
                p.applyNld(v0);
                final Optional<Bot> secondary = state.getBotByPos(p);
                assert secondary.isPresent() : "failed to fusionP " + targetBot.pos + " and " + p;
                targetBot.seeds.add(secondary.get().bid);
                targetBot.seeds.addAll(secondary.get().seeds);
                state.addEnergy(-24);

            }
        },
        FUSIONS {
            @Override
            public void execute(final State state, final Bot targetBot, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {
                final Coordinate p = targetBot.pos.clone();
                p.applyNld(v0);
                final Optional<Bot> secondary = state.getBotByPos(p);
                assert secondary.isPresent() : "failed to fusionS " + p + " and " + targetBot.pos;
                p.applyNld(secondary.get().getAssignedTrace().val0);
                assert targetBot.pos.equals(p) : "fusion is not symmetric " + p + " and " + targetBot.pos;
            }
        },
        FISSION {
            @Override
            public void execute(final State state, final Bot targetBot, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {
                final Coordinate p = targetBot.pos.clone();
                p.applyNld(v0);
                final Bot newBot = new Bot(targetBot.seeds.get(0), p, targetBot.seeds.subList(1, v1 + 1));
                targetBot.seeds = targetBot.seeds.subList(v1 + 1, targetBot.seeds.size());
                state.getBots().add(newBot);
                state.addEnergy(24);
            }
        },
        FILL {
            @Override
            public void execute(final State state, final Bot targetBot, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {
                final Coordinate p = targetBot.pos.clone();
                p.applyNld(v0);
//                System.out.printf("filled (%d, %d, %d)\n", p.x, p.y, p.z);
                if (state.getBoard().fill(p)) {
                    state.addEnergy(12);
                } else {
                    state.addEnergy(6);
                }
            }
        };

        public void execute(final State state, final Bot targetBot, final Integer v0, final Integer v1, final Integer v2, final Integer v3) {
            throw new UnsupportedOperationException();
        }
    }
}
