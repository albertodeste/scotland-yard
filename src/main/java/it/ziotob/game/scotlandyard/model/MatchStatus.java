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
            return "place_players";
        } else {
            return "unknown";
        }
    }

    private boolean isPlacePlayers() {
        return match.getStarted() && !players.stream().allMatch(Player::isPlaced);
    }

    private boolean isAddPlayers() {
        return !match.getStarted();
    }

    public String toJSON() {

        return String.format(
                "{\"status\": \"%s\"}",
                getStatusMessage()
        );
    }
}
