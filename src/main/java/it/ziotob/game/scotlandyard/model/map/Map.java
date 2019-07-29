package it.ziotob.game.scotlandyard.model.map;

import it.ziotob.game.scotlandyard.model.residuals.ResidualMoves;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.ziotob.game.scotlandyard.model.map.Graph.ConnectionType.*;
import static java.util.Arrays.asList;

public class Map {

    public static final List<Long> POSITIONS_PLAYERS = asList(197L, 112L, 53L, 132L, 91L, 198L, 94L, 155L, 174L, 103L, 34L, 13L, 26L, 29L, 138L, 141L, 50L, 117L);
    public static final List<Long> POSITIONS_MISTER_X = asList(10L, 12L, 100L, 120L); //TODO load form real cards

    private static final Graph graph = buildGraph();

    private static Graph buildGraph() {

        Graph result = new Graph();

        result.connect(1L, LOW, 8L, 9L);
        result.connect(1L, MID, 46L, 58L);
        result.connect(1L, 46L, HIGH);

        result.connect(2L, LOW, 10L, 20L);

        result.connect(3L, LOW, 4L, 11L, 12L);
        result.connect(3L, MID, 22L, 23L);

        result.connect(4L, 13L, LOW);

        result.connect(5L, LOW, 15L, 16L);

        result.connect(6L, LOW, 7L, 29L);

        result.connect(7L, 17L, LOW);
        result.connect(7L, 42L, MID);

        result.connect(8L, LOW, 18L, 19L);

        result.connect(9L, LOW, 19L, 20L);

        result.connect(10L, LOW, 21L, 34L, 11L);

        result.connect(11L, 22L, LOW);

        result.connect(12L, 23L, LOW);

        result.connect(13L, LOW, 23L, 14L, 24L);
        result.connect(13L, MID, 23L, 14L);
        result.connect(13L, HIGH, 46L, 67L, 89L);

        result.connect(14L, LOW, 15L, 25L);
        result.connect(14L, MID, 15L);

        result.connect(15L, LOW, 16L, 26L, 28L);
        result.connect(15L, MID, 41L, 29L);

        result.connect(16L, LOW, 28L, 29L);

        result.connect(17L, LOW, 29L, 30L);

        result.connect(18L, LOW, 43L, 31L);

        result.connect(19L, 32L, LOW);

        result.connect(20L, 33L, LOW);

        result.connect(21L, 33L, LOW);

        result.connect(22L, LOW, 34L, 23L, 35L);
        result.connect(22L, MID, 34L, 23L, 65L);

        result.connect(23L, 37L, LOW);
        result.connect(23L, 67L, MID);

        result.connect(24L, LOW, 37L, 38L);

        result.connect(25L, LOW, 38L, 39L);

        result.connect(26L, LOW, 27L, 39L);

        result.connect(27L, LOW, 28L, 40L);

        result.connect(28L, 41L, LOW);

        result.connect(29L, LOW, 41L, 42L);
        result.connect(29L, MID, 41L, 42L, 55L);

        result.connect(30L, 42L, LOW);

        result.connect(31L, LOW, 43L, 44L);

        result.connect(32L, LOW, 33L, 44L, 45L);

        result.connect(33L, 46L, LOW);

        result.connect(34L, LOW, 47L, 48L);
        result.connect(34L, MID, 46L, 63L);

        result.connect(35L, LOW, 36L, 48L);

        result.connect(36L, LOW, 37L, 49L);

        result.connect(37L, 50L, LOW);

        result.connect(38L, LOW, 50L, 51L);

        result.connect(39L, LOW, 51L, 52L);

        result.connect(40L, LOW, 41L, 52L, 53L);

        result.connect(41L, 54L, LOW);
        result.connect(41L, MID, 52L, 87L);

        result.connect(42L, LOW, 56L, 72L);
        result.connect(42L, 72L, LOW);

        result.connect(43L, 57L, LOW);

        result.connect(44L, 58L, LOW);

        result.connect(45L, LOW, 58L, 59L, 60L, 46L);

        result.connect(46L, LOW, 47L, 61L);
        result.connect(46L, MID, 58L, 78L);
        result.connect(46L, HIGH, 74L, 79L);

        result.connect(47L, 62L, LOW);

        result.connect(48L, LOW, 62L, 63L);

        result.connect(49L, LOW, 50L, 66L);

        result.connect(50L, 67L, LOW);

        //TODO add next connections

        return result;
    }

    public static boolean validateMove(Long position, Long endPosition, String moveType) {
        return graph.isConnected(position, endPosition, moveType);
    }

    public static List<Long> getReachablePositions(Long position, ResidualMoves residual) {

        return Optional.ofNullable(graph.getConnections(position))
                .map(map -> map.entrySet().stream())
                .orElse(Stream.empty())
                .filter(entry -> residual.canDoMove(entry.getValue().getValue()))
                .map(java.util.Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
