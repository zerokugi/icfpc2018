package icfpc2018.models;

import com.google.common.collect.Lists;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

    public Trace copy() {
        return new Trace(type, val0, val1,  val2, val3);
    }

    public Trace reverse() {
        return this.type.reverse(this);
    }

    public enum Type {
        HALT,
        WAIT {
            @Override
            public void execute(final State state, final Bot targetBot, final Trace trace, final boolean validate) {
                if (validate) {
                    validVolatility(state, targetBot.pos);
                } else {
                    unfill(state, targetBot.pos);
                }
            }

            @Override
            public Trace reverse(final Trace trace) {
                return new Trace(WAIT);
            }
        },
        FLIP {
            @Override
            public void execute(final State state, final Bot targetBot, final Trace trace, final boolean validate) {
//                System.out.printf("fliped\n");
                if (validate) {
                    validVolatility(state, targetBot.pos);
                } else {
                    unfill(state, targetBot.pos);
                    state.flipHarmonics();
                }
            }
            @Override
            public Trace reverse(final Trace trace) {
                return new Trace(FLIP);
            }
        },
        SMOVE {
            @Override
            public void execute(final State state, final Bot targetBot, final Trace trace, final boolean validate) {
                final Coordinate p = targetBot.pos.clone();
                p.applyLld(trace.val0, trace.val1);
                if (validate) {
//                    System.out.printf("validate smove (%d, %d, %d)\n", p.x, p.y, p.z);
                    assert state.getBoard().in(p)
                            : "can not smove outside of the board " + p;
                    validVolatility(state, targetBot.pos, p);
                } else {
                    unfill(state, targetBot.pos, p);
                    targetBot.pos = p;
                    state.addEnergy(2 * Math.abs(trace.val1 - 15));
                }
            }

            @Override
            public Trace reverse(Trace trace) {
                return new Trace(SMOVE, trace.val0, 30 - trace.val1);
            }
        },
        LMOVE {
            @Override
            public void execute(final State state, final Bot targetBot, final Trace trace, final boolean validate) {
                final Coordinate p = targetBot.pos.clone();
                p.applySld(trace.val0, trace.val1);
                final Coordinate mid = p.clone();
                p.applySld(trace.val2, trace.val3);
                if (validate) {
//                    System.out.printf("validate lmove (%d, %d, %d)\n", p.x, p.y, p.z);
                    assert state.getBoard().in(mid)
                            : "can not LMOVE(1) to outside of the board " + mid;
                    assert state.getBoard().in(p)
                            : "can not LMOVE(2) to outside of the board " + p;
                    validVolatility(state, targetBot.pos, mid);
                    unfill(state, mid);
                    validVolatility(state, mid, p);
                } else {
                    unfill(state, targetBot.pos, mid);
                    validVolatility(state, mid);
                    unfill(state, mid, p);
                    targetBot.pos = p;
                    state.addEnergy(2 * (Math.abs(trace.val1 - 5) + 2 + Math.abs(trace.val3 - 5)));
                }
            }

            @Override
            public Trace reverse(Trace trace) {
                return new Trace(LMOVE, trace.val2, 10 - trace.val3, trace.val0, 10 - trace.val1);
            }
        },
        FUSIONP {
            @Override
            public void execute(final State state, final Bot targetBot, final Trace trace, final boolean validate) {
                final Coordinate p = targetBot.pos.clone();
                p.applyNld(trace.val0);
                final Optional<Bot> secondary = state.getBotByPos(p);
                if (validate) {
                    assert secondary.isPresent() : "failed to fusionP " + targetBot.pos + " and " + p;
                    assert secondary.get().getAssignedTrace().type == FUSIONS
                            : "secondary bot is not assigned fusion S";
                    validVolatility(state, targetBot.pos);
                } else {
                    unfill(state, targetBot.pos);
                    targetBot.seeds.add(secondary.get().bid);
                    targetBot.seeds.addAll(secondary.get().seeds);
                    state.addEnergy(-24);
                }
            }

            @Override
            public Trace reverse(Trace trace) {
                throw new NotImplementedException();
            }
        },
        FUSIONS {
            @Override
            public void execute(final State state, final Bot targetBot, final Trace trace, final boolean validate) {
                final Coordinate p = targetBot.pos.clone();
                if (validate) {
                    validVolatility(state, targetBot.pos);
                    p.applyNld(trace.val0);
                    final Optional<Bot> primary = state.getBotByPos(p);
                    assert primary.isPresent() : "failed to fusionS " + p + " and " + targetBot.pos;
                    assert primary.get().getAssignedTrace().type == FUSIONP
                            : "primary bot is not assigned fusion P";
                    p.applyNld(primary.get().getAssignedTrace().val0);
                    assert targetBot.pos.equals(p) : "fusion is not symmetric " + p + " and " + targetBot.pos;
                } else {
                    unfill(state, p);
                }
            }

            @Override
            public Trace reverse(Trace trace) {
                throw new NotImplementedException();
            }
        },
        FISSION {
            @Override
            public void execute(final State state, final Bot targetBot, final Trace trace, final boolean validate) {
                final Coordinate p = targetBot.pos.clone();
                p.applyNld(trace.val0);
                if (validate) {
                    validVolatility(state, targetBot.pos);
                    validVolatility(state, p);
                    assert state.getBoard().in(p)
                            : "can not fission outside of the board " + p;
                } else {
                    unfill(state, targetBot.pos);
                    unfill(state, p);
                    final Bot newBot = new Bot(targetBot.seeds.get(0), p, Lists.newArrayList(targetBot.seeds.subList(1, trace.val1 + 1)));
                    targetBot.seeds = targetBot.seeds.subList(trace.val1 + 1, targetBot.seeds.size());
                    state.getBots().add(newBot);
                    state.addEnergy(24);
                }
            }

            @Override
            public Trace reverse(Trace trace) {
                throw new NotImplementedException();
            }
        },
        FILL {
            @Override
            public void execute(final State state, final Bot targetBot, final Trace trace, final boolean validate) {
                final Coordinate p = targetBot.pos.clone();
                p.applyNld(trace.val0);
                if (validate) {
//                    System.out.printf("validate fill (%d, %d, %d)\n", p.x, p.y, p.z);
                    validVolatility(state, targetBot.pos);
                    validVolatility(state, p);
                    final int R = state.getBoard().getR();
                    assert 0 < Math.min(p.x, p.z) && Math.max(p.x, p.z) < R - 1 && 0 <= p.y && p.y < R
                            : "can not fill " + p;
                } else {
                    unfill(state, targetBot.pos);
                    unfill(state, p);
                    state.getBoard().fill(p);
                    state.addEnergy(12);
                }
            }

            @Override
            public Trace reverse(Trace trace) {
                return new Trace(VOID, trace.val0);
            }
        },
        VOID {
            @Override
            public void execute(final State state, final Bot targetBot, final Trace trace, final boolean validate) {
                final Coordinate p = targetBot.pos.clone();
                p.applyNld(trace.val0);
                if (validate) {
//                    System.out.printf("validate void (%d, %d, %d)\n", p.x, p.y, p.z);
                    validVolatility(state, targetBot.pos);
                    final int R = state.getBoard().getR();
                    assert 0 < Math.min(p.x, p.z) && Math.max(p.x, p.z) < R - 1 && 0 <= p.y && p.y < R
                            : "out of bounds to void " + p;
                } else {
                    unfill(state, targetBot.pos);
                    if (state.getBoard().get(p)) {
                        state.getBoard().doVoid(p);
                        state.addEnergy(-12);
                    } else {
                        state.addEnergy(3);
                    }
                }
            }

            @Override
            public Trace reverse(Trace trace) {
                return new Trace(FILL, trace.val0);
            }
        },
        GFILL,
        GVOID;

        public void execute(final State state, final Bot targetBot, final Trace trace, final boolean validate) {
            throw new UnsupportedOperationException();
        }

        public Trace reverse(final Trace trace) {
            return trace.type.reverse(trace);
        }

        void validVolatility(final State state, final Coordinate p) {
            assert state.getBoard().flip(p.x, p.y, p.z, 1) : "Volatile confliction: " + p.toString();
        }

        void validVolatility(final State state, final Coordinate p1, final Coordinate p2) {
            for (int x = Math.min(p1.x, p2.x); x <= Math.max(p1.x, p2.x); x++) {
                for (int y = Math.min(p1.y, p2.y); y <= Math.max(p1.y, p2.y); y++) {
                    for (int z = Math.min(p1.z, p2.z); z <= Math.max(p1.z, p2.z); z++) {
                        validVolatility(state, new Coordinate(x, y, z));
                    }
                }
            }
        }

        void unfill(final State state, final Coordinate p) {
            assert state.getBoard().flip(p.x, p.y, p.z, 0) : "Failed to unfill: " + p.toString();
        }

        void unfill(final State state, final Coordinate p1, final Coordinate p2) {
            for (int x = Math.min(p1.x, p2.x); x <= Math.max(p1.x, p2.x); x++) {
                for (int y = Math.min(p1.y, p2.y); y <= Math.max(p1.y, p2.y); y++) {
                    for (int z = Math.min(p1.z, p2.z); z <= Math.max(p1.z, p2.z); z++) {
                        unfill(state, new Coordinate(x, y, z));
                    }
                }
            }
        }
    }
}
