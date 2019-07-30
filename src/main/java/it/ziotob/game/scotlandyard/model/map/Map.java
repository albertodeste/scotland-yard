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
    public static final List<Long> POSITIONS_MISTER_X = asList(10L, 12L, 100L, 120L); //TODO load from real cards

    private static final Graph graph = buildGraph();

    private static Graph buildGraph() {

        Graph result = new Graph();

        result.connect(1, LOW, 8, 9);
        result.connect(1, MID, 46, 58);
        result.connect(1, HIGH, 46);

        result.connect(2, LOW, 10, 20);

        result.connect(3, LOW, 4, 11, 12);
        result.connect(3, MID, 22, 23);

        result.connect(4, LOW, 13);

        result.connect(5, LOW, 15, 16);

        result.connect(6, LOW, 7, 29);

        result.connect(7, LOW, 17);
        result.connect(7, MID, 42);

        result.connect(8, LOW, 18, 19);

        result.connect(9, LOW, 19, 20);

        result.connect(10, LOW, 21, 34, 11);

        result.connect(11, LOW, 22);

        result.connect(12, LOW, 23);

        result.connect(13, LOW, 23, 14, 24);
        result.connect(13, MID, 23, 14);
        result.connect(13, HIGH, 46, 67, 89);

        result.connect(14, LOW, 15, 25);
        result.connect(14, MID, 15);

        result.connect(15, LOW, 16, 26, 28);
        result.connect(15, MID, 41, 29);

        result.connect(16, LOW, 28, 29);

        result.connect(17, LOW, 29, 30);

        result.connect(18, LOW, 43, 31);

        result.connect(19, LOW, 32);

        result.connect(20, LOW, 33);

        result.connect(21, LOW, 33);

        result.connect(22, LOW, 34, 23, 35);
        result.connect(22, MID, 34, 23, 65);

        result.connect(23, LOW, 37);
        result.connect(23, MID, 67);

        result.connect(24, LOW, 37, 38);

        result.connect(25, LOW, 38, 39);

        result.connect(26, LOW, 27, 39);

        result.connect(27, LOW, 28, 40);

        result.connect(28, LOW, 41);

        result.connect(29, LOW, 41, 42);
        result.connect(29, MID, 41, 42, 55);

        result.connect(30, LOW, 42);

        result.connect(31, LOW, 43, 44);

        result.connect(32, LOW, 33, 44, 45);

        result.connect(33, LOW, 46);

        result.connect(34, LOW, 47, 48);
        result.connect(34, MID, 46, 63);

        result.connect(35, LOW, 36, 48);

        result.connect(36, LOW, 37, 49);

        result.connect(37, LOW, 50);

        result.connect(38, LOW, 50, 51);

        result.connect(39, LOW, 51, 52);

        result.connect(40, LOW, 41, 52, 53);

        result.connect(41, LOW, 54);
        result.connect(41, MID, 52, 87);

        result.connect(42, LOW, 56, 72);
        result.connect(42, LOW, 72);

        result.connect(43, LOW, 57);

        result.connect(44, LOW, 58);

        result.connect(45, LOW, 58, 59, 60, 46);

        result.connect(46, LOW, 47, 61);
        result.connect(46, MID, 58, 78);
        result.connect(46, HIGH, 74, 79);

        result.connect(47, LOW, 62);

        result.connect(48, LOW, 62, 63);

        result.connect(49, LOW, 50, 66);

        result.connect(50, LOW, 67);

        result.connect(51, LOW, 52, 67, 68);

        result.connect(52, LOW, 69);
        result.connect(52, MID, 67, 86);

        result.connect(53, LOW, 69, 54);

        result.connect(54, LOW, 55, 70);

        result.connect(55, LOW, 71);
        result.connect(55, MID, 89);

        result.connect(56, LOW, 91);

        result.connect(57, LOW, 58, 73);

        result.connect(58, LOW, 59, 74);
        result.connect(58, MID, 74, 77);

        result.connect(59, LOW, 75, 76);

        result.connect(60, LOW, 61, 76);

        result.connect(61, LOW, 62, 78, 76);

        result.connect(62, LOW, 79);

        result.connect(63, LOW, 64, 79, 80);
        result.connect(63, MID, 65, 78, 100);

        result.connect(64, LOW, 65, 81);

        result.connect(65, LOW, 66, 82);

        result.connect(66, LOW, 67, 82);

        result.connect(67, LOW, 68, 84);
        result.connect(67, MID, 82, 102);
        result.connect(67, HIGH, 79, 89, 111);

        result.connect(68, LOW, 69, 85);

        result.connect(69, LOW, 86);

        result.connect(70, LOW, 71, 87);

        result.connect(71, LOW, 72, 89);

        result.connect(72, LOW, 90, 91);
        result.connect(72, MID, 105, 107);

        result.connect(73, LOW, 74, 92);

        result.connect(74, LOW, 75, 92, 94);
        result.connect(74, MID, 94);

        result.connect(75, LOW, 94);

        result.connect(76, LOW, 77);

        result.connect(77, LOW, 78, 95, 96);
        result.connect(77, MID, 78, 94, 124);

        result.connect(78, LOW, 79, 97);
        result.connect(78, MID, 79);

        result.connect(79, LOW, 98);
        result.connect(79, HIGH, 111);

        result.connect(80, LOW, 99, 100);

        result.connect(81, LOW, 82, 100);

        result.connect(82, LOW, 101);
        result.connect(82, MID, 100, 140);

        result.connect(83, LOW, 101, 102);

        result.connect(84, LOW, 85);

        result.connect(85, LOW, 103);

        result.connect(86, LOW, 103, 104);
        result.connect(86, MID, 87, 102, 116);

        result.connect(87, LOW, 88);
        result.connect(87, MID, 105);

        result.connect(88, LOW, 89, 117);

        result.connect(89, LOW, 105);
        result.connect(89, MID, 105);
        result.connect(89, HIGH, 140, 128);

        result.connect(90, LOW, 91, 105);

        result.connect(91, LOW, 105, 107);

        result.connect(92, LOW, 93);

        result.connect(93, LOW, 94);
        result.connect(93, MID, 94);

        result.connect(94, LOW, 95);

        result.connect(95, LOW, 122);

        result.connect(96, LOW, 97, 109);

        result.connect(97, LOW, 98, 109);

        result.connect(98, LOW, 99, 110);

        result.connect(99, LOW, 110, 112);

        result.connect(100, LOW, 101, 112, 113);
        result.connect(100, MID, 111);

        //TODO map remaining map nodes

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
