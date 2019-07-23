package it.ziotob.game.scotlandyard.service;

import it.ziotob.game.scotlandyard.model.Match;
import it.ziotob.game.scotlandyard.model.MatchStatus;
import it.ziotob.game.scotlandyard.model.Player;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchStatusService {

    private static MatchStatusService instance;

    public static MatchStatusService getInstance() {

        if (isNull(instance)) {
            instance = new MatchStatusService();
        }
        return instance;
    }

    public MatchStatus getMatchStatus(Match match) {

        List<Player> players = PlayerService.getInstance().getPlayers(match.getRelatedPlayerIds()).collect(Collectors.toList());

        return new MatchStatus(match, players);
    }

    public MatchStatus getMatchStatus(Player player) {

        Match match = MatchService.getInstance().getMatch(player.getMatchId()).orElseThrow(() -> new RuntimeException("Player with id " + player.getId() + " associated to not existing match " + player.getMatchId() + " while placing misterX"));

        return getMatchStatus(match);
    }
}
