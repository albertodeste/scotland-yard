package it.ziotob.game.scotlandyard.service;

import it.ziotob.game.scotlandyard.database.Database;
import it.ziotob.game.scotlandyard.model.Match;
import it.ziotob.game.scotlandyard.repository.MatchRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Objects.isNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchService {

    private final MatchRepository matchRepository;

    private static MatchService instance;

    public static MatchService getInstance() {

        if (isNull(instance)) {
            instance = new MatchService(new MatchRepository(Database.getInstance()));
        }

        return instance;
    }

    public String createMatch() {
        return matchRepository.createMatch(LocalDateTime.now());
    }

    public Optional<Match> getMatch(String matchId) {
        return matchRepository.getMatch(matchId);
    }

    public void addPlayer(Match match, String playerId, LocalDateTime dateTime) {
        matchRepository.addPlayer(match, playerId, dateTime);
    }
}
