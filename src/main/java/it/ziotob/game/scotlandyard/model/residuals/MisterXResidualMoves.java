package it.ziotob.game.scotlandyard.model.residuals;

import it.ziotob.game.scotlandyard.model.MatchStatus;
import it.ziotob.game.scotlandyard.model.Player;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.ziotob.game.scotlandyard.model.residuals.DetectiveResidualMoves.*;

@RequiredArgsConstructor
public class MisterXResidualMoves extends ResidualMoves {

    public static final Long DEFAULT_MISTER_X_MOVES = 5L;
    public static final Long DEFAULT_DOUBLE_MOVES = 2L;

    private final MatchStatus matchStatus;

    @Override
    protected void populateCache(Map<String, Long> cache) {

        Player misterX = matchStatus.getPlayers().stream().filter(Player::isMisterX).findFirst().orElseThrow(() -> new RuntimeException("Trying to determine misterX residual moves without misterX in list"));
        List<DetectiveResidualMoves> playersResiduals = matchStatus.getPlayers().stream().filter(Player::isDetective).map(DetectiveResidualMoves::new).collect(Collectors.toList());

        if (matchStatus.isFirstRound()) {

            cache.put(MOVE_LOW, 1L);
            cache.put(MOVE_MID, 1L);
            cache.put(MOVE_HIGH, 1L);
        } else {

            cache.put(MOVE_LOW, playersResiduals.stream().mapToLong(a -> DEFAULT_LOW_MOVES - a.getMoveTypeResidual(MOVE_LOW)).sum() - misterX.getMoveLow());
            cache.put(MOVE_MID, playersResiduals.stream().mapToLong(a -> DEFAULT_MID_MOVES - a.getMoveTypeResidual(MOVE_MID)).sum() - misterX.getMoveMid());
            cache.put(MOVE_HIGH, playersResiduals.stream().mapToLong(a -> DEFAULT_HIGH_MOVES - a.getMoveTypeResidual(MOVE_HIGH)).sum() - misterX.getMoveHigh());
        }

        cache.put(MOVE_MISTER_X, DEFAULT_MISTER_X_MOVES - misterX.getMoveMisterX());
        cache.put(MOVE_DOUBLE, DEFAULT_DOUBLE_MOVES - misterX.getMoveDouble());
    }
}
