package icfpc2018;

import icfpc2018.models.Bot;
import icfpc2018.models.Coordinate;
import icfpc2018.models.State;

public class Validator {
    public static void validateHalt(final State state) {
        assert state.getBots().size() == 1 : "can not halt if more than one bots";
        assert state.getBots().get(0).pos.equals(new Coordinate(0, 0, 0))
                : "can halt only if bot is located on (0, 0, 0)";
        assert state.getHarmonics() == State.Harmonics.LOW
                : "can halt only if harmonics is LOW";
    }

    public static void validateGlobal(final State state) {
        assert (state.getHarmonics() == State.Harmonics.HIGH) || !state.getBoard().mustBeUngrounded()
                : "must be grounded if harmonics is low";

        long idSet = 0;
        for (final Bot bot : state.getBots()) {
            assert ((idSet & (1L << bot.bid)) == 0)
                    : "conflicting bid " + bot.bid;
            idSet |= 1L << bot.bid;
            assert !state.getBoard().get(bot.pos)
                    : "bot can't be located on the filled voxel";
            for (final int seed : bot.seeds) {
                assert ((idSet & (1L << seed)) == 0)
                        : "conflicting bid " + seed;
                idSet |= 1L << seed;
            }
        }
    }
}
