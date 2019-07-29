package it.ziotob.game.scotlandyard.model.residuals;

import it.ziotob.game.scotlandyard.model.map.Graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class ResidualMoves {

    public static final String MOVE_LOW = Graph.ConnectionType.LOW.getValue();
    public static final String MOVE_MID = Graph.ConnectionType.MID.getValue();
    public static final String MOVE_HIGH = Graph.ConnectionType.HIGH.getValue();
    public static final String MOVE_MISTER_X = Graph.ConnectionType.MISTER_X.getValue();
    public static final String MOVE_DOUBLE = "double";

    private Map<String, Long> cache = new HashMap<>();

    private Map<String, Long> getResidual() {

        if (cache.isEmpty()) {
            populateCache(cache);
        }

        return cache;
    }

    protected abstract void populateCache(Map<String, Long> cache);

    public boolean canDoMove(String moveType) {

        return getResidual().entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .map(Map.Entry::getKey)
                .anyMatch(availableMove -> Objects.equals(availableMove, moveType));
    }

    public String toJSONString() {
        return "{}"; //TODO implement
    }

    public Long getMoveTypeResidual(String moveType) {

        return getResidual().entrySet().stream()
                .filter(e -> e.getKey().equals(moveType))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(0L);
    }
}
