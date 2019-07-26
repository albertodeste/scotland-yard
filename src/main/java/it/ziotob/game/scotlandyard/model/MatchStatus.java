package it.ziotob.game.scotlandyard.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
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
        } else if (isMisterXWin()) {
            return "mister_x_wins";
        } else if (isDetectivesWin()) {
            return "detectives_win";
        } else {
            return "unknown";
        }
    }

    private boolean isDetectivesWin() {
        return false; //TODO implement
    }

    private boolean isMisterXWin() {
        return false; //TODO implement
    }

    private boolean isMovePlayers() {

        boolean allPlayersPlaced = players.stream().allMatch(Player::isPlaced) && !players.isEmpty();
        boolean allDetectivesCanMove = players.stream().filter(Player::isDetective).map(Player::getResidualRoundMoves).allMatch(residual -> residual > 0);
        boolean misterXCanMove = players.stream().filter(Player::isMisterX).map(Player::getResidualRoundMoves).allMatch(residual -> residual > 0);

        return allPlayersPlaced && allDetectivesCanMove && !misterXCanMove;
    }

    private boolean isMoveMisterX() {

        boolean allPlayersPlaced = players.stream().allMatch(Player::isPlaced) && !players.isEmpty();
        boolean allPlayersCanMove = players.stream().map(Player::getResidualRoundMoves).allMatch(residual -> residual > 0);

        return allPlayersPlaced && allPlayersCanMove;
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

        return isAddPlayers() && misterXExists && minimumPlayersReached && !maximumPlayersReached;
    }

    public boolean canPlacePlayers() {

        boolean matchHasPositions = !match.getPositions().isEmpty();
        boolean matchHasFreePositions = match.getPositions().stream().filter(p -> !p.getMisterX()).anyMatch(p -> !p.getUsed());

        return matchHasPositions && matchHasFreePositions && isPlacePlayers();
    }

    public boolean canAddPlayers() {
        return isAddPlayers();
    }
}
