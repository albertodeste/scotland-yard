package it.ziotob.game.scotlandyard.handler;

import it.ziotob.game.scotlandyard.model.Match;
import it.ziotob.game.scotlandyard.model.MatchStatus;
import it.ziotob.game.scotlandyard.model.Player;
import it.ziotob.game.scotlandyard.service.MatchService;
import it.ziotob.game.scotlandyard.service.PlayerService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.ziotob.game.scotlandyard.utils.ServletUtils.getPathVariables;

public class MatchHandler extends HttpServlet {

    private static final String BASE_URL = "match";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String matchId = MatchService.getInstance().createMatch();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(String.format("{ \"id\": \"%s\" }", matchId));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<String> pathVariables = getPathVariables(request, BASE_URL);
        String matchId = pathVariables.stream().findFirst().orElseThrow(() -> new RuntimeException("Match GET called without matchId"));
        String action = pathVariables.stream().skip(1L).findFirst().orElse("");

        Optional<Match> match = MatchService.getInstance().getMatch(matchId);

        if (!match.isPresent()) {

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if ("status".equals(action)) {
            getMatchStatus(match.get(), response);
        } else {
            getMatchInfo(match.get(), response);
        }
    }

    private void getMatchStatus(Match match, HttpServletResponse response) throws IOException {

        List<Player> players = PlayerService.getInstance().getPlayers(match.getRelatedPlayerIds()).collect(Collectors.toList());

        MatchStatus status = new MatchStatus(match, players);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().println(status.toJSON());
    }

    private void getMatchInfo(Match match, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(match.toJSON());
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) {

        List<String> pathVariables = getPathVariables(request, BASE_URL);
        String matchId = pathVariables.stream().findFirst().orElseThrow(() -> new RuntimeException("Match PUT called without matchId"));
        String action = pathVariables.stream().skip(1L).findFirst().orElse("");

        if ("start".equals(action)) {
            startMatch(matchId, response);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void startMatch(String matchId, HttpServletResponse response) {

        Optional<Match> matchOpt = MatchService.getInstance().getMatch(matchId);

        if (matchOpt.isPresent()) {

            if (MatchService.getInstance().startMatch(matchOpt.get())) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
