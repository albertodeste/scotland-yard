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

	protected void doPut(HttpServletRequest request, HttpServletResponse response) {

		List<String> pathVariables = getPathVariables(request, BASE_URL);
		String playerId = pathVariables.stream().findFirst().orElseThrow(() -> new RuntimeException("Player PUT called without playerId"));
		String action = pathVariables.stream().skip(1L).findFirst().orElse("");

		Optional<Player> player = PlayerService.getInstance().getPlayer(playerId);

		if (!player.isPresent()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		if ("place".equals(action)) {
			placePlayer(player.get(), pathVariables, response);
		} else if ("move".equals(action)) {
			movePlayer(player.get(), pathVariables, response);
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private void placePlayer(Player player, List<String> pathVariables, HttpServletResponse response) {

		String playerPosition = pathVariables.stream().skip(2L).findFirst()
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

	private void movePlayer(Player player, List<String> pathVariables, HttpServletResponse response) {

		String moveType = pathVariables.stream().skip(2L).findFirst()
				.orElseThrow(() -> new RuntimeException("Trying to call Player PUT move without move type"));
		Long endingPoition = pathVariables.stream().skip(3L).findFirst().map(Long::parseLong)
				.orElseThrow(() -> new RuntimeException("Trying to call Player PUT move without ending position"));

		if (PlayerService.getInstance().movePlayer(player, endingPoition, moveType)) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String playerId = getPathVariables(request, BASE_URL).stream().findFirst()
				.orElseThrow(() -> new RuntimeException("Trying to call Player GET without playerId"));
		Optional<Player> player = PlayerService.getInstance().getPlayer(playerId);

		if (player.isPresent()) {

			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(player.get().toJSON());
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
