package it.ziotob.game.scotlandyard.model;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MatchStatus {

    private final Match match;
    private final List<Player> players;

    public String getStatusMessage() {

        if (isAddPlayers()) {
            return "add_players";
        } else if (isPlacePlayers()) {
            return "set_players_position";
        } else if (isMoveMisterX()) {
            return "move_mister_x";
        } else if (isMovePlayers()) {
            return "move_players";
        } else {
            return "unknown";
        }
    }

    private boolean isMovePlayers() {
        return true; //TODO implement
    }

    private boolean isMoveMisterX() {
        return true; //TODO implement
    }

    private boolean isPlacePlayers() {
        return match.getStarted() && !players.stream().allMatch(Player::isPlaced);
    }

    private boolean isAddPlayers() {
        return !match.getStarted();
    }

    public String toJSON() {

        return String.format(
                "{\"status\": \"%s\", \"can_start\": %s}",
                getStatusMessage(),
                canStart().toString()
        );
    }

    public Boolean canStart() {

        boolean misterXExists = players.stream().anyMatch(Player::isMisterX);
        boolean minimumPlayersReached = players.size() > 1;
        boolean maximumPlayersReached = players.size() > 4;

        //TODO check that random positions has been calculated for this match

        return isAddPlayers() && misterXExists && minimumPlayersReached && !maximumPlayersReached;
    }
}
