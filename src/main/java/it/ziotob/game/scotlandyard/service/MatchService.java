package it.ziotob.game.scotlandyard.service;

import it.ziotob.game.scotlandyard.database.Database;
import it.ziotob.game.scotlandyard.model.Match;
import it.ziotob.game.scotlandyard.model.MatchStatus;
import it.ziotob.game.scotlandyard.model.Player;
import it.ziotob.game.scotlandyard.model.Position;
import it.ziotob.game.scotlandyard.repository.MatchRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.ziotob.game.scotlandyard.model.map.Map.POSITIONS_MISTER_X;
import static it.ziotob.game.scotlandyard.model.map.Map.POSITIONS_PLAYERS;
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
        matchRepository.addPlayer(match, playerId, dateTime);
    }

    public boolean startMatch(Match match) {

        List<Player> players = PlayerService.getInstance().getPlayers(match.getRelatedPlayerIds()).collect(Collectors.toList());
        MatchStatus matchStatus = new MatchStatus(match, players);

        if (matchStatus.canStart()) {

            LocalDateTime dateTime = LocalDateTime.now();

            matchRepository.startMatch(match, dateTime);
            generatePositions(match, dateTime);
            return true;
        } else {
            return false;
        }
    }

    private void generatePositions(Match match, LocalDateTime dateTime) {

        final Random random = new Random();

        List<Position> positions = Stream.generate(() -> random.nextInt(POSITIONS_PLAYERS.size() - 1))
                .distinct()
                .limit(match.getRelatedPlayerIds().size() - 1)
                .map(POSITIONS_PLAYERS::get)
                .map(Position::new)
                .collect(Collectors.toList());

        Position misterXPosition = new Position(POSITIONS_MISTER_X.get(random.nextInt(POSITIONS_MISTER_X.size() - 1)));
        misterXPosition.setMisterX();

        Stream.concat(positions.stream(), Stream.of(misterXPosition))
                .forEach(position -> matchRepository.addPosition(match, position, dateTime));
    }
}
