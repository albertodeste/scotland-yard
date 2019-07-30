package it.ziotob.game.scotlandyard.model.residuals;

import it.ziotob.game.scotlandyard.model.Player;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class DetectiveResidualMoves extends ResidualMoves {

    private final Player detective;

    public static final Long DEFAULT_LOW_MOVES = 13L;
    public static final Long DEFAULT_MID_MOVES = 8L;
    public static final Long DEFAULT_HIGH_MOVES = 3L;

    @Override
    protected void populateCache(Map<String, Long> cache) {

        cache.put(MOVE_LOW, DEFAULT_LOW_MOVES - detective.getMoveLow().longValue());
        cache.put(MOVE_MID, DEFAULT_MID_MOVES - detective.getMoveMid().longValue());
        cache.put(MOVE_HIGH, DEFAULT_HIGH_MOVES - detective.getMoveHigh().longValue());
        cache.put(MOVE_MISTER_X, 0L);
        cache.put(MOVE_DOUBLE, 0L);
    }
}
