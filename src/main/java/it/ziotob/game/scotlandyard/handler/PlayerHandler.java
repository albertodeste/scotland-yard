package it.ziotob.game.scotlandyard.handler;

import it.ziotob.game.scotlandyard.model.Match;
import it.ziotob.game.scotlandyard.model.MatchStatus;
import it.ziotob.game.scotlandyard.model.Player;
import it.ziotob.game.scotlandyard.model.residuals.ResidualMoves;
import it.ziotob.game.scotlandyard.service.MatchService;
import it.ziotob.game.scotlandyard.service.MatchStatusService;
import it.ziotob.game.scotlandyard.service.PlayerService;
import it.ziotob.game.scotlandyard.utils.ServletUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static it.ziotob.game.scotlandyard.utils.ServletUtils.getPathVariables;

public class PlayerHandler extends HttpServlet {

    private static final String BASE_URL = "player";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Map<String, Object> requestBody = ServletUtils.parseBody(request);
        List<String> pathVariables = getPathVariables(request, BASE_URL);

        String matchId = pathVariables.stream().findFirst().orElseThrow(() -> new RuntimeException("Player POST called without matchId"));
        String name = (String) requestBody.get("name");
        String role = (String) requestBody.get("role");

        Optional<Match> match = MatchService.getInstance().getMatch(matchId);

        if (!match.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<String> playerId = PlayerService.getInstance().createPlayer(match.get(), name, role);

        if (playerId.isPresent()) {

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(String.format("{ \"id\": \"%s\" }", playerId.get()));
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Map<String, Object> requestBody = ServletUtils.parseBody(request);
        List<String> pathVariables = getPathVariables(request, BASE_URL);
        String playerId = pathVariables.stream().findFirst().orElseThrow(() -> new RuntimeException("Player PUT called without playerId"));
        String action = pathVariables.stream().skip(1L).findFirst().orElse("");

        Optional<Player> player = PlayerService.getInstance().getPlayer(playerId);

        if (!player.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if ("place".equals(action)) {
            placePlayer(player.get(), requestBody, response);
        } else if ("move".equals(action)) {
            movePlayer(player.get(), requestBody, response);
        } else if ("double".equals(action)) {
            doubleMovePlayer(player.get(), response);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void doubleMovePlayer(Player player, HttpServletResponse response) {

        if (PlayerService.getInstance().doubleMovePlayer(player)) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void placePlayer(Player player, Map<String, Object> requestBody, HttpServletResponse response) {

        String playerPosition = Optional.ofNullable(requestBody.get("position"))
                .map(obj -> (String) obj)
                .orElseThrow(() -> new RuntimeException("Trying to call Player PUT place without position"));
        boolean isMisterXPosition = "mister_x".equals(playerPosition);

        if (isMisterXPosition) {

            if (PlayerService.getInstance().placeMisterX(player)) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else if (PlayerService.getInstance().placePlayer(player, Long.parseLong(playerPosition))) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void movePlayer(Player player, Map<String, Object> requestBody, HttpServletResponse response) {

        String moveType = Optional.ofNullable(requestBody.get("move_type"))
                .map(obj -> (String) obj)
                .orElseThrow(() -> new RuntimeException("Trying to call Player PUT move without move type"));
        Long endingPoition = Optional.ofNullable(requestBody.get("destination"))
                .map(obj -> (Long) obj)
                .orElseThrow(() -> new RuntimeException("Trying to call Player PUT move without ending position"));

        if (PlayerService.getInstance().movePlayer(player, endingPoition, moveType)) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<String> pathVariables = getPathVariables(request, BASE_URL);

        String playerId = pathVariables.stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Trying to call Player GET without playerId"));
        String action = pathVariables.stream().skip(1L).findFirst().orElse("get");

        Optional<Player> player = PlayerService.getInstance().getPlayer(playerId);

        if (!player.isPresent()) {

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if ("get".equals(action)) {
            getPlayer(player.get(), response);
        } else if ("residuals".equals(action)) {
            getResiduals(player.get(), response);
        }
    }

    private void getResiduals(Player player, HttpServletResponse response) throws IOException {

        MatchStatus matchStatus = MatchStatusService.getInstance().getMatchStatus(player);
        ResidualMoves residuals = PlayerService.getInstance().getPlayerResidualMoves(player, matchStatus);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().println(residuals.toJSON());
    }

    private void getPlayer(Player player, HttpServletResponse response) throws IOException {

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().println(player.toJSON());
    }
}
