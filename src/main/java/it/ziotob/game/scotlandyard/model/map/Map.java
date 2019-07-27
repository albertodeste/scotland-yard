package it.ziotob.game.scotlandyard.model.map;

import java.util.List;

import static java.util.Arrays.asList;

public class Map {

    public static final List<Long> POSITIONS_PLAYERS = asList(197L, 112L, 53L, 132L, 91L, 198L, 94L, 155L, 174L, 103L, 34L, 13L, 26L, 29L, 138L, 141L, 50L, 117L);
    public static final List<Long> POSITIONS_MISTER_X = asList(10L, 12L, 100L, 120L);

    public static boolean validateMove(Long position, Long endPosition, String moveType) {

    	//TODO validate position connected to end position through moveType
    	return true; //TODO implement
    }
}
