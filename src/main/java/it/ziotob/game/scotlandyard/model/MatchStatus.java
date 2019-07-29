package it.ziotob.game.scotlandyard.model;

import it.ziotob.game.scotlandyard.model.map.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

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
        } else if (isDetectivesWin()) {
            return "detectives_win";
        } else if (isMisterXWin()) {
            return "mister_x_wins";
        } else if (isMoveMisterX()) {
            return "move_mister_x";
        } else if (isMovePlayers()) {
            return "move_players";
        } else {
            return "unknown";
        }
    }

    private boolean isDetectivesWin() {

        Long misterXPosition = players.stream().filter(Player::isMisterX).findFirst().map(Player::getPosition).orElse(-1L);
        List<Player> detectives = players.stream().filter(Player::isDetective).collect(Collectors.toList());
        boolean detectiveOverMisterX = detectives.stream().map(Player::getPosition).anyMatch(position -> Objects.equals(position, misterXPosition));

        List<String> misterXResiduals = asList("low", "mid", "high", "mister_x"); //TODO retrieve information from misterXResiduals

        List<Long> misterXReachablePositions = Map.getReachablePositions(misterXPosition, misterXResiduals);
        List<Long> filteredMisterXReachablePositions = misterXReachablePositions.stream()
                .filter(possiblePosition -> detectives.stream().map(Player::getPosition).noneMatch(detectivePosition -> Objects.equals(possiblePosition, detectivePosition)))
                .collect(Collectors.toList());

        return detectiveOverMisterX || !filteredMisterXReachablePositions.isEmpty();
    }

    private boolean isMisterXWin() {

        Integer roundNumber = players.stream().filter(Player::isMisterX).findFirst().map(Player::getCurrentRound).orElse(0);

        return isMoveMisterX() && roundNumber > 24;
    }

    private boolean isMovePlayers() {

        boolean allPlayersPlaced = players.stream().allMatch(Player::isPlaced) && !players.isEmpty();
        boolean anyDetectiveCanMove = players.stream().filter(Player::isDetective).map(Player::getResidualRoundMoves).anyMatch(residual -> residual > 0);
        boolean misterXCanMove = players.stream().filter(Player::isMisterX).map(Player::getResidualRoundMoves).allMatch(residual -> residual > 0);

        return allPlayersPlaced && anyDetectiveCanMove && !misterXCanMove;
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
                "{\"status\": \"%s\", \"can_start\": %s, \"round\": %d}",
                getStatusMessage(),
                canStart().toString(),
                getRound()
        );
    }

    private Integer getRound() {
        return players.stream().filter(Player::isMisterX).findFirst().map(Player::getCurrentRound).orElse(0);
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

    public boolean canMoveMisterX() {
        return isMoveMisterX();
    }

    public boolean canMoveDetective() {
        return isMovePlayers();
    }
}
