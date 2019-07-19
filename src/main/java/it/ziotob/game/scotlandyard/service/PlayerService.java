package it.ziotob.game.scotlandyard.service;

import it.ziotob.game.scotlandyard.database.Database;
import it.ziotob.game.scotlandyard.model.Match;
import it.ziotob.game.scotlandyard.model.Player;
import it.ziotob.game.scotlandyard.repository.PlayerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    public String createPlayer(Match match, String name, String role) {

        LocalDateTime dateTime = LocalDateTime.now();
        String playerId = playerRepository.createPlayer(dateTime, name, role);

        MatchService.getInstance().addPlayer(match, playerId, dateTime);

        return playerId;
    }

    public Optional<Player> getPlayer(String playerId) {
        return getPlayers(singletonList(playerId)).findFirst();
    }

    public Stream<Player> getPlayers(List<String> playerIds) {
        return playerRepository.getPlayers(playerIds);
    }

    public boolean placePlayer(Player player, Long position) {

        //TODO check match is in correct phase (set_players_position, move_players, move_mister_x)
        //TODO check that player position can be set (correct phase and not placement happened or position has been picked up from random positions and not already been assigned)
        //TODO check validity of movement through map
        return playerRepository.placePlayer(player, position, LocalDateTime.now());
    }
}
