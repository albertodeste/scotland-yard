package it.ziotob.game.scotlandyard.service;

import it.ziotob.game.scotlandyard.database.Database;
import it.ziotob.game.scotlandyard.model.Match;
import it.ziotob.game.scotlandyard.model.MatchStatus;
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

    public Match createMatch() {

        String matchId = matchRepository.createMatch(LocalDateTime.now(), generateMatchJoinString());
        return getMatch(matchId).orElseThrow(() -> new RuntimeException("Cannot retrieve created match with id " + matchId));
    }

    private String generateMatchJoinString() {
        return generateRandomString(4) + "-" + generateRandomString(4);
    }

    private String generateRandomString(int length) {

        String possibilities = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();

        return Stream.generate(() -> random.nextInt(possibilities.length()))
                .limit(length)
                .map(possibilities::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    public Optional<Match> getMatch(String matchId) {
        return matchRepository.getMatches(singletonList(matchId)).findFirst();
    }

    public void addPlayer(Match match, String playerId, LocalDateTime dateTime) {
        matchRepository.addPlayer(match, playerId, dateTime);
    }

    public boolean startMatch(Match match) {

        MatchStatus matchStatus = MatchStatusService.getInstance().getMatchStatus(match);

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

    public boolean canSetPositionAtInstant(String matchId, Long number, LocalDateTime dateTime) {

        return matchRepository.getMatches(singletonList(matchId), dateTime).findFirst()
                .map(m -> m.getPositions().stream())
                .orElse(Stream.empty())
                .anyMatch(position -> !position.getUsed() && number.equals(position.getNumber()));
    }

    public boolean usePosition(Match match, Long position, LocalDateTime dateTime) {
        return matchRepository.usePosition(match, position, dateTime);
    }
}
