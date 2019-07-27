package it.ziotob.game.scotlandyard.repository;

import it.ziotob.game.scotlandyard.database.Database;
import it.ziotob.game.scotlandyard.database.Event;
import it.ziotob.game.scotlandyard.model.Player;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.ziotob.game.scotlandyard.model.Player.*;

@RequiredArgsConstructor
public class PlayerRepository {

    private final Database database;

    public String createPlayer(LocalDateTime dateTime, String name, String role, String matchId) {

        String id = UUID.randomUUID().toString();

        database.putEvent(new Event(id, Player.EVENT_CREATE, null, dateTime), Player.GROUP);
        database.putEvent(new Event(id, Player.EVENT_SET_NAME, name, dateTime), Player.GROUP);
        database.putEvent(new Event(id, Player.EVENT_SET_ROLE, role, dateTime), Player.GROUP);
        database.putEvent(new Event(id, Player.EVENT_SET_MATCH_ID, matchId, dateTime), Player.GROUP);

        return id;
    }

    public Stream<Player> getPlayers(List<String> playerIds) {

        return database.getEvents(Player.GROUP)
                .collect(Collectors.groupingBy(Event::getId))
                .entrySet().stream()
                .parallel()
                .filter(entry -> playerIds.contains(entry.getKey()))
                .map(playerEvents -> Player.buildFromEvents(playerEvents.getValue().stream()))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public void placePlayer(Player player, Long position, LocalDateTime dateTime) {
        database.putEvent(new Event(player.getId(), Player.EVENT_SET_POSITION, position.toString(), dateTime), Player.GROUP);
    }

    public void nextRound(Player player, LocalDateTime dateTime) {
        database.putEvent(new Event(player.getId(), Player.EVENT_NEW_ROUND, null, dateTime), Player.GROUP);
    }

	public void movePlayer(Player player, Long endPosition, String moveType, LocalDateTime dateTime) {
        database.putEvent(new Event(player.getId(), mapMoveTypeToEvent(moveType), endPosition.toString(), dateTime), Player.GROUP);
	}

    private String mapMoveTypeToEvent(String moveType) {

        if ("low".equals(moveType)) {
            return EVENT_MOVE_LOW;
        } else if ("mid".equals(moveType)) {
            return EVENT_MOVE_MID;
        } else if ("high".equals(moveType)) {
            return EVENT_MOVE_HIGH;
        } else if ("mister_x".equals(moveType)) {
            return EVENT_MOVE_MISTER_X;
        } else {
            throw new RuntimeException("Move type " + moveType + " not mappable for persistency");
        }
    }
}
