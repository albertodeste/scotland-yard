package it.ziotob.game.scotlandyard.service;

import it.ziotob.game.scotlandyard.database.Database;
import it.ziotob.game.scotlandyard.model.Match;
import it.ziotob.game.scotlandyard.model.MatchStatus;
import it.ziotob.game.scotlandyard.model.Player;
import it.ziotob.game.scotlandyard.repository.PlayerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerService {

    private final PlayerRepository playerRepository;

    private static PlayerService instance;

    public static PlayerService getInstance() {

        if (isNull(instance)) {
            instance = new PlayerService(new PlayerRepository(Database.getInstance()));
        }

        return instance;
    }

    public Optional<String> createPlayer(Match match, String name, String role) {

        List<Player> players = getPlayers(match.getRelatedPlayerIds()).collect(Collectors.toList());

        MatchStatus matchStatus = new MatchStatus(match, players);

        if (matchStatus.canAddPlayers()) {

            LocalDateTime dateTime = LocalDateTime.now();
            String playerId = playerRepository.createPlayer(dateTime, name, role, match.getId());

            MatchService.getInstance().addPlayer(match, playerId, dateTime);

            return Optional.of(playerId);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Player> getPlayer(String playerId) {
        return getPlayers(singletonList(playerId)).findFirst();
    }

    public Stream<Player> getPlayers(List<String> playerIds) {
        return playerRepository.getPlayers(playerIds);
    }

    public boolean placePlayer(Player player, Long position) {

        Match match = MatchService.getInstance().getMatch(player.getMatchId()).orElseThrow(() -> new RuntimeException("Player with id " + player.getId() + " associated to not existing match " + player.getMatchId() + " while placing player"));
        List<Player> players = PlayerService.getInstance().getPlayers(match.getRelatedPlayerIds()).collect(Collectors.toList());
        MatchStatus matchStatus = new MatchStatus(match, players);

        boolean isValidPosition = match.getPositions().stream().filter(p -> p.getMisterX() == player.isMisterX()).filter(p -> !p.getUsed()).anyMatch(p -> position.equals(p.getNumber()));

        if (matchStatus.canPlacePlayers() && !player.isPlaced() && isValidPosition) {
            return playerRepository.placePlayer(player, position, LocalDateTime.now());
        } else {
            return false;
        }
    }

    public boolean movePlayer(Player player, Long startingPosition, Long endingPosition) {

        //TODO validate game in correct phase (move_players, move_mister_x)
        //TODO validate move is correct through map
        //TODO validate no other detectives in this position
        return true;
    }
}
