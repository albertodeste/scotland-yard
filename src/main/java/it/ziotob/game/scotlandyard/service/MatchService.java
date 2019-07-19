package it.ziotob.game.scotlandyard.service;

import it.ziotob.game.scotlandyard.database.Database;
import it.ziotob.game.scotlandyard.model.Match;
import it.ziotob.game.scotlandyard.model.MatchStatus;
import it.ziotob.game.scotlandyard.model.Player;
import it.ziotob.game.scotlandyard.repository.MatchRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
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
        return matchRepository.getMatches(singletonList(matchId)).findFirst();
    }

    public void addPlayer(Match match, String playerId, LocalDateTime dateTime) {
        //TODO return error if match already started
        matchRepository.addPlayer(match, playerId, dateTime);
    }

    public boolean startMatch(Match match) {

        List<Player> players = PlayerService.getInstance().getPlayers(match.getRelatedPlayerIds()).collect(Collectors.toList());
        MatchStatus matchStatus = new MatchStatus(match, players);

        if (matchStatus.canStart()) {

            matchRepository.startMatch(match, LocalDateTime.now());
            return true;
        } else {
            return false;
        }
    }
}
