package it.ziotob.game.scotlandyard.handler;

import it.ziotob.game.scotlandyard.model.Match;
import it.ziotob.game.scotlandyard.model.MatchStatus;
import it.ziotob.game.scotlandyard.model.Position;
import it.ziotob.game.scotlandyard.service.MatchService;
import it.ziotob.game.scotlandyard.service.MatchStatusService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.ziotob.game.scotlandyard.utils.FormatUtils.formatList;
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
        } else if ("positions".equals(action)) {
            getMatchPositions(match.get(), response);
        } else {
            getMatchInfo(match.get(), response);
        }
    }

    private void getMatchPositions(Match match, HttpServletResponse response) throws IOException {

        List<Position> positions = match.getPositions().stream().filter(p -> !p.getMisterX()).collect(Collectors.toList());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().println(formatList(positions, Position::toJSON));
    }

    private void getMatchStatus(Match match, HttpServletResponse response) throws IOException {

        MatchStatus status = MatchStatusService.getInstance().getMatchStatus(match);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().println(status.toJSON());
    }

    private void getMatchInfo(Match match, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(match.toJSON());
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<String> pathVariables = getPathVariables(request, BASE_URL);
        String matchId = pathVariables.stream().findFirst().orElseThrow(() -> new RuntimeException("Match PUT called without matchId"));
        String action = pathVariables.stream().skip(1L).findFirst().orElse("");

        Optional<Match> match = MatchService.getInstance().getMatch(matchId);

        if (!match.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if ("start".equals(action)) {
            startMatch(match.get(), response);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void startMatch(Match match, HttpServletResponse response) {

        if (MatchService.getInstance().startMatch(match)) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
