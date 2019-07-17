package it.ziotob.game.scotlandyard.handler;

import it.ziotob.game.scotlandyard.model.Match;
import it.ziotob.game.scotlandyard.model.Player;
import it.ziotob.game.scotlandyard.service.MatchService;
import it.ziotob.game.scotlandyard.service.PlayerService;
import it.ziotob.game.scotlandyard.utils.ServletUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class PlayerHandler extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Map<String, Object> requestBody = ServletUtils.parseBody(request);

        String matchId = request.getRequestURI().replaceAll("^.*/player/", "").replaceAll("/.*$", "");
        String name = (String) requestBody.get("name");
        Boolean isMisterX = (Boolean) requestBody.get("is_mister_x");

        Optional<Match> matchOpt = MatchService.getInstance().getMatch(matchId);
        Optional<String> playerIdOpt = matchOpt.map(match -> PlayerService.getInstance().createPlayer(match, name, isMisterX));

        if (playerIdOpt.isPresent()) {

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(String.format("{ \"id\": \"%s\" }", playerIdOpt.get()));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String playerId = request.getRequestURI().replaceAll("^.*/player/", "");
        Optional<Player> player = PlayerService.getInstance().getPlayer(playerId);

        if (player.isPresent()) {

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(player.get().toJSON());
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
