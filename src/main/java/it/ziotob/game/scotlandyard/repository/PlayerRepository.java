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

@RequiredArgsConstructor
public class PlayerRepository {

    private final Database database;

    public String createPlayer(LocalDateTime dateTime, String name, String role) {

        String id = UUID.randomUUID().toString();

        database.putEvent(new Event(id, Player.EVENT_CREATE, null, dateTime), Player.GROUP);
        database.putEvent(new Event(id, Player.EVENT_SET_NAME, name, dateTime), Player.GROUP);
        database.putEvent(new Event(id, Player.EVENT_SET_ROLE, role, dateTime), Player.GROUP);

        return id;
    }

    public Stream<Player> getPlayers(List<String> playerIds) {

        return database.getEvents(Player.GROUP)
                .collect(Collectors.groupingBy(Event::getId))
                .entrySet().stream()
                .map(playerEvents -> Player.buildFromEvents(playerEvents.getValue().stream()))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }
}
