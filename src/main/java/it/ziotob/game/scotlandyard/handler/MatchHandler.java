package it.ziotob.game.scotlandyard.handler;

import it.ziotob.game.scotlandyard.model.Match;
import it.ziotob.game.scotlandyard.service.MatchService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class MatchHandler extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String matchId = MatchService.getInstance().createMatch();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(String.format("{ \"id\": \"%s\" }", matchId));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String matchId = request.getRequestURI().replaceAll("^.*/match/", "");
        Optional<Match> match = MatchService.getInstance().getMatch(matchId);

        if (match.isPresent()) {

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(match.get().toJSON());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) {

        String[] pathVariables = request.getRequestURI().replaceAll("^.*/match/", "").split("/");
        String matchId = pathVariables[0];
        String action = pathVariables[1];

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
