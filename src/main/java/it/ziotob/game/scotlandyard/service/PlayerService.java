package it.ziotob.game.scotlandyard.service;

import it.ziotob.game.scotlandyard.database.Database;
import it.ziotob.game.scotlandyard.model.Match;
import it.ziotob.game.scotlandyard.model.Player;
import it.ziotob.game.scotlandyard.repository.PlayerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

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
        return playerRepository.getPlayer(playerId);
    }
}
