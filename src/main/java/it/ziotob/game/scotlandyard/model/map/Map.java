package it.ziotob.game.scotlandyard.model.map;

import it.ziotob.game.scotlandyard.model.residuals.ResidualMoves;

import java.util.List;

import static it.ziotob.game.scotlandyard.model.map.Graph.ConnectionType.*;
import static java.util.Arrays.asList;

public class Map {

    public static final List<Long> POSITIONS_PLAYERS = asList(197L, 112L, 53L, 123L, 91L, 198L, 94L, 155L, 174L, 103L, 34L, 13L, 26L, 29L, 138L, 141L, 50L, 117L);
    public static final List<Long> POSITIONS_MISTER_X = asList(104L, 170L, 106L, 166L, 146L, 35L, 51L, 132L, 45L, 127L, 78L, 172L, 71L);

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
        result.connect(13, MID, 23, 14, 52);
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

        result.connect(35, LOW, 36, 48, 65);

        result.connect(36, LOW, 37, 49);

        result.connect(37, LOW, 50);

        result.connect(38, LOW, 50, 51);

        result.connect(39, LOW, 51, 52);

        result.connect(40, LOW, 41, 52, 53);

        result.connect(41, LOW, 54);
        result.connect(41, MID, 52, 87);

        result.connect(42, LOW, 56, 72);
        result.connect(42, MID, 72);

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

        result.connect(58, LOW, 59, 74, 75);
        result.connect(58, MID, 74, 77);

        result.connect(59, LOW, 75, 76);

        result.connect(60, LOW, 61, 76);

        result.connect(61, LOW, 62, 78, 76);

        result.connect(62, LOW, 79);

        result.connect(63, LOW, 64, 79, 80);
        result.connect(63, MID, 65, 79, 100);

        result.connect(64, LOW, 65, 81);

        result.connect(65, LOW, 66, 82);
        result.connect(65, MID, 67, 82);

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

        result.connect(74, LOW, 75, 92);
        result.connect(74, MID, 94);

        result.connect(75, LOW, 94);

        result.connect(76, LOW, 77);

        result.connect(77, LOW, 78, 95, 96);
        result.connect(77, MID, 78, 94, 124);

        result.connect(78, LOW, 79, 97);
        result.connect(78, MID, 79);

        result.connect(79, LOW, 98);
        result.connect(79, HIGH, 93, 111);

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

        result.connect(101, LOW, 114);

        result.connect(102, LOW, 103, 115);
        result.connect(102, MID, 127);

        result.connect(104, LOW, 116);

        result.connect(105, LOW, 106, 108);
        result.connect(105, MID, 107, 108);

        result.connect(106, LOW, 107);

        result.connect(107, LOW, 119);
        result.connect(107, MID, 161);

        result.connect(108, LOW, 117, 119);
        result.connect(108, MID, 116, 135);
        result.connect(108, MISTER_X, 115);

        result.connect(109, LOW, 110, 124);

        result.connect(110, LOW, 111);

        result.connect(111, LOW, 112, 124);
        result.connect(111, MID, 124);
        result.connect(111, HIGH, 153, 163);

        result.connect(112, LOW, 125);

        result.connect(113, LOW, 114, 125);

        result.connect(114, LOW, 115, 126, 131, 132);

        result.connect(115, LOW, 126, 127);
        result.connect(115, MISTER_X, 157);

        result.connect(116, LOW, 117, 118, 127);
        result.connect(116, MID, 127, 142);

        result.connect(117, LOW, 129);

        result.connect(118, LOW, 129, 134, 142);

        result.connect(119, LOW, 136);

        result.connect(120, LOW, 121, 144);

        result.connect(121, LOW, 122, 145);

        result.connect(122, LOW, 123, 146);
        result.connect(122, MID, 123, 144);

        result.connect(123, LOW, 124, 137, 148, 149);
        result.connect(123, MID, 124, 144, 165);

        result.connect(124, LOW, 130, 138);
        result.connect(124, MID, 153);

        result.connect(125, LOW, 131);

        result.connect(126, LOW, 127, 140);

        result.connect(127, LOW, 133, 134);
        result.connect(127, MID, 133);

        result.connect(128, LOW, 142, 143, 160, 188, 172);
        result.connect(128, MID, 135, 142, 161, 187, 199);
        result.connect(128, HIGH, 140, 185);

        result.connect(129, LOW, 135, 142, 143);

        result.connect(130, LOW, 131, 139);

        result.connect(132, LOW, 140);

        result.connect(133, LOW, 134, 140, 141);
        result.connect(133, MID, 140, 157);

        result.connect(134, LOW, 141, 142);

        result.connect(135, LOW, 136, 143, 161);

        result.connect(136, LOW, 162);

        result.connect(137, LOW, 147);

        result.connect(138, LOW, 150, 152);

        result.connect(139, LOW, 153, 154);

        result.connect(140, LOW, 154, 156);
        result.connect(140, MID, 154, 156);
        result.connect(140, HIGH, 153);

        result.connect(141, LOW, 142, 158);

        result.connect(142, LOW, 143, 158);
        result.connect(142, MID, 157);

        result.connect(143, LOW, 160);

        result.connect(144, LOW, 145, 177);
        result.connect(144, MID, 163);

        result.connect(145, LOW, 146);

        result.connect(146, LOW, 147, 163);

        result.connect(147, LOW, 164);

        result.connect(148, LOW, 149, 164);

        result.connect(149, LOW, 150, 165);

        result.connect(150, LOW, 151);

        result.connect(151, LOW, 152, 165, 166);

        result.connect(152, LOW, 153);

        result.connect(153, LOW, 154, 166, 167);
        result.connect(153, MID, 154, 180, 184);
        result.connect(153, HIGH, 163, 185);

        result.connect(154, LOW, 155);
        result.connect(154, MID, 156);

        result.connect(155, LOW, 156, 167, 168);

        result.connect(156, LOW, 157, 169);
        result.connect(156, MID, 157, 184);

        result.connect(157, LOW, 158, 170);
        result.connect(157, MID, 185);
        result.connect(157, MISTER_X, 194);

        result.connect(158, LOW, 159);

        result.connect(159, LOW, 170, 172, 186, 198);

        result.connect(160, LOW, 161, 173);

        result.connect(161, LOW, 174);
        result.connect(161, MID, 199);

        result.connect(162, LOW, 175);

        result.connect(163, LOW, 177);
        result.connect(163, MID, 176, 191);

        result.connect(164, LOW, 178, 179);

        result.connect(165, LOW, 179, 180);
        result.connect(165, MID, 180, 191);

        result.connect(166, LOW, 181, 183);

        result.connect(167, LOW, 168, 183);

        result.connect(168, LOW, 184);

        result.connect(169, LOW, 184);

        result.connect(170, LOW, 185);

        result.connect(171, LOW, 173, 175, 199);

        result.connect(172, LOW, 187);

        result.connect(173, LOW, 174, 188);

        result.connect(174, LOW, 175);

        result.connect(176, LOW, 177, 189);
        result.connect(176, MID, 190);

        result.connect(178, LOW, 189, 191);

        result.connect(179, LOW, 191);

        result.connect(180, LOW, 181, 193);
        result.connect(180, MID, 190, 184);

        result.connect(181, LOW, 182, 193);

        result.connect(182, LOW, 183, 195);

        result.connect(183, LOW, 196);

        result.connect(184, LOW, 185, 196, 197);
        result.connect(184, MID, 185);

        result.connect(185, LOW, 186);
        result.connect(185, MID, 187);

        result.connect(186, LOW, 198);

        result.connect(187, LOW, 188, 198);

        result.connect(188, LOW, 199);

        result.connect(189, LOW, 190);

        result.connect(190, LOW, 191, 192);
        result.connect(190, MID, 191);

        result.connect(191, LOW, 192);

        result.connect(192, LOW, 194);

        result.connect(193, LOW, 194);

        result.connect(194, LOW, 195);

        result.connect(195, LOW, 197);

        result.connect(196, LOW, 197);

        result.connect(198, LOW, 199);

        return result;
    }

    public static boolean validateMove(Long position, Long endPosition, String moveType) {
        return graph.isConnected(position, endPosition, moveType);
    }

    public static List<Long> getReachablePositions(Long position, ResidualMoves residual) {
        return graph.getReachablePositions(position, residual.getResidualNames());
    }
}
