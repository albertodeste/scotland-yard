package it.ziotob.game.scotlandyard.repository;

import it.ziotob.game.scotlandyard.database.Database;
import it.ziotob.game.scotlandyard.database.Event;
import it.ziotob.game.scotlandyard.model.Match;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class MatchRepository {

    private final Database database;

    public String createMatch(LocalDateTime dateTime) {

        String uid = UUID.randomUUID().toString();

        database.putEvent(new Event(uid, Match.EVENT_CREATE, null, dateTime), Match.GROUP);

        return uid;
    }

    public Optional<Match> getMatch(String id) {

        return Match.buildFromEvents(database.getEvents(Match.GROUP)
                .filter(event -> id.equals(event.getId())));
    }

    public void addPlayer(Match match, String playerId, LocalDateTime dateTime) {
        database.putEvent(new Event(match.getId(), Match.EVENT_ADDED_PLAYER, playerId, dateTime), Match.GROUP);
    }
}
