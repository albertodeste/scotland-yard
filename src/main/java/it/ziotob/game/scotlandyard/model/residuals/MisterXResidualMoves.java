package it.ziotob.game.scotlandyard.model.residuals;

import it.ziotob.game.scotlandyard.model.MatchStatus;
import it.ziotob.game.scotlandyard.model.Player;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class MisterXResidualMoves extends ResidualMoves {

    private static final Long DEFAULT_MISTER_X_MOVES = 5L;
    private static final Long DEFAULT_DOUBLE_MOVES = 2L;

    private final MatchStatus matchStatus;

    @Override
    protected void populateCache(Map<String, Long> cache) {

        Player misterX = matchStatus.getPlayers().stream().filter(Player::isMisterX).findFirst().orElseThrow(() -> new RuntimeException("Trying to determine misterX residual moves without misterX in list"));

        Long lowResiduals = 0L; //TODO elaborate
        Long midResiduals = 0L; //TODO elaborate
        Long highResiduals = 0L; //TODO elaborate

        cache.put(MOVE_LOW, lowResiduals);
        cache.put(MOVE_MID, midResiduals);
        cache.put(MOVE_HIGH, highResiduals);
        cache.put(MOVE_MISTER_X, DEFAULT_MISTER_X_MOVES - misterX.getMoveMisterX());
        cache.put(MOVE_DOUBLE, DEFAULT_DOUBLE_MOVES - misterX.getMoveDouble());
    }
}
