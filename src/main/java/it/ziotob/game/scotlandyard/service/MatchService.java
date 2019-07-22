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

import static java.util.Arrays.asList;
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

            LocalDateTime dateTime = LocalDateTime.now();

            matchRepository.startMatch(match, dateTime);
            generatePositions(match, dateTime);
            return true;
        } else {
            return false;
        }
    }

    private List<Position> generatePositions(Match match, LocalDateTime dateTime) {

        final Random random = new Random();

        final List<Long> mapPositions = asList(197L, 112L, 53L, 132L, 91L, 198L, 94L, 155L, 174L, 103L, 34L, 13L, 26L, 29L, 138L, 141L, 50L, 117L); //TODO move into Map.Positions
        final List<Long> misterXPositions = asList(10L, 12L, 100L, 120L); //TODO move into Map.PositionsMisterX

        List<Position> positions = Stream.generate(() -> random.nextInt(mapPositions.size() - 1))
                .distinct()
                .limit(match.getRelatedPlayerIds().size() - 1)
                .map(mapPositions::get)
                .map(Position::new)
                .collect(Collectors.toList());

        Position misterXPosition = new Position(misterXPositions.get(random.nextInt(misterXPositions.size() - 1)));
        misterXPosition.setMisterX();

        return Stream.concat(positions.stream(), Stream.of(misterXPosition))
                .peek(position -> matchRepository.addPosition(match, position, dateTime))
                .collect(Collectors.toList());
    }
}
