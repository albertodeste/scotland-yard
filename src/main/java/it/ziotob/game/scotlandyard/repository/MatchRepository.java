package it.ziotob.game.scotlandyard.repository;

import it.ziotob.game.scotlandyard.database.Database;
import it.ziotob.game.scotlandyard.database.Event;
import it.ziotob.game.scotlandyard.model.Match;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class MatchRepository {

    private final Database database;

    public String createMatch(LocalDateTime dateTime) {

        String uid = UUID.randomUUID().toString();

        database.putEvent(new Event(uid, Match.EVENT_CREATE, null, dateTime), Match.GROUP);

        return uid;
    }

    public Stream<Match> getMatches(List<String> matchIds) {

        return database.getEvents(Match.GROUP)
                .collect(Collectors.groupingBy(Event::getId))
                .entrySet().stream()
                .filter(entry -> matchIds.contains(entry.getKey()))
                .map(matchEvents -> Match.buildFromEvents(matchEvents.getValue().stream()))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public void addPlayer(Match match, String playerId, LocalDateTime dateTime) {
        database.putEvent(new Event(match.getId(), Match.EVENT_ADDED_PLAYER, playerId, dateTime), Match.GROUP);
    }

    public void startMatch(Match match, LocalDateTime dateTime) {
        database.putEvent(new Event(match.getId(), Match.EVENT_START, null, dateTime), Match.GROUP);
    }
}
