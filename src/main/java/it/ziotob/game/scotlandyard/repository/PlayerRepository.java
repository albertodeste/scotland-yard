package it.ziotob.game.scotlandyard.repository;

import it.ziotob.game.scotlandyard.database.Database;
import it.ziotob.game.scotlandyard.database.Event;
import it.ziotob.game.scotlandyard.model.Player;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayerRepository {

    private final Database database;

    public String createPlayer(LocalDateTime dateTime, String name, Boolean isMisterX) {

        String id = UUID.randomUUID().toString();

        database.putEvent(new Event(id, Player.EVENT_CREATE, null, dateTime), Player.GROUP);
        database.putEvent(new Event(id, Player.EVENT_SET_NAME, name, dateTime), Player.GROUP);
        database.putEvent(new Event(id, Player.EVENT_SET_MISTER_X, isMisterX.toString(), dateTime), Player.GROUP);

        return id;
    }

    public Optional<Player> getPlayer(String id) {

        return Player.buildFromEvents(database.getEvents(Player.GROUP)
                .filter(event -> id.equals(event.getId())));
    }
}
