package it.ziotob.game.scotlandyard.service;

import it.ziotob.game.scotlandyard.database.Database;
import it.ziotob.game.scotlandyard.model.Match;
import it.ziotob.game.scotlandyard.model.MatchStatus;
import it.ziotob.game.scotlandyard.model.Player;
import it.ziotob.game.scotlandyard.model.Position;
import it.ziotob.game.scotlandyard.model.map.Map;
import it.ziotob.game.scotlandyard.repository.PlayerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE) public class PlayerService {

	private final PlayerRepository playerRepository;

	private static PlayerService instance;

	public static PlayerService getInstance() {

		if (isNull(instance)) {
			instance = new PlayerService(new PlayerRepository(Database.getInstance()));
		}

		return instance;
	}

	public Optional<String> createPlayer(Match match, String name, String role) {

		MatchStatus matchStatus = MatchStatusService.getInstance().getMatchStatus(match);

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

		MatchStatus matchStatus = MatchStatusService.getInstance().getMatchStatus(player);
		Match match = matchStatus.getMatch();

		boolean isValidPosition = match.getPositions().stream().filter(p -> p.getMisterX() == player.isMisterX()).filter(p -> !p.getUsed())
				.anyMatch(p -> position.equals(p.getNumber()));

		if (matchStatus.canPlacePlayers() && !player.isPlaced() && isValidPosition) {

			LocalDateTime dateTime = LocalDateTime.now();
			MatchService.getInstance().usePosition(match, position, dateTime);
			playerRepository.placePlayer(player, position, dateTime);

			if (allPlayersPlacedExcept(matchStatus.getPlayers(), player)) {
				nextRound(matchStatus.getPlayers());
			}

			return true;
		} else {
			return false;
		}
	}

	public boolean movePlayer(Player player, Long endingPosition, String moveType) {

		if (player.isMisterX()) {
			return moveMisterX(player, endingPosition, moveType);
		} else {
			return moveDetective(player, endingPosition, moveType);
		}
	}

	private boolean moveMisterX(Player player, Long endPosition, String moveType) {

		MatchStatus matchStatus = MatchStatusService.getInstance().getMatchStatus(player);

		Supplier<Boolean> validator = () -> {

			boolean validMove = Map.validateMove(player.getPosition(), endPosition, moveType);
			boolean playerCanMoveInTurn = player.getResidualRoundMoves() > 0;
			boolean validResiduals = true; //TODO validate moveType is valid on player's residuals
			boolean emptyCell = true; //TODO validate no other players are in this position (exclude misterX)

			return validMove && playerCanMoveInTurn && validResiduals && emptyCell;
		};

		if (matchStatus.canMoveMisterX() && validator.get()) {

			playerRepository.movePlayer(player, endPosition, moveType, LocalDateTime.now());
			return true;
		} else {
			return false;
		}
	}

	private boolean moveDetective(Player player, Long endPosition, String moveType) {

		MatchStatus matchStatus = MatchStatusService.getInstance().getMatchStatus(player);

		Supplier<Boolean> validator = () -> {

			boolean validMove = Map.validateMove(player.getPosition(), endPosition, moveType);
			boolean playerCanMoveInTurn = player.getResidualRoundMoves() > 0;
			boolean validResiduals = true; //TODO validate player residuals are valid for the wanted move
			boolean emptyCell = true; //TODO validate no other detectives are in this position (exclude misterX)

			return validMove && playerCanMoveInTurn && validResiduals && emptyCell;
		};

		Supplier<Boolean> allPlayersMoved = () -> matchStatus.getPlayers().stream()
				.filter(p -> !p.getId().equals(player.getId()))
				.allMatch(p -> p.getResidualRoundMoves() <= 0);

		if (matchStatus.canMoveDetective() && validator.get()) {

			playerRepository.movePlayer(player, endPosition, moveType, LocalDateTime.now());

			if (allPlayersMoved.get()) {
				LocalDateTime dateTime = LocalDateTime.now();
				matchStatus.getPlayers().forEach(p -> playerRepository.nextRound(p, dateTime));
			}

			return true;
		} else {
			return false;
		}
	}

	public boolean placeMisterX(Player player) {

		MatchStatus matchStatus = MatchStatusService.getInstance().getMatchStatus(player);
		Match match = matchStatus.getMatch();

		Optional<Position> position = match.getPositions().stream().filter(Position::getMisterX).findFirst();
		boolean misterXPositionValid = position.filter(p -> !p.getUsed()).isPresent() && !match.getPositions().isEmpty();

		if (player.isMisterX() && matchStatus.canPlacePlayers() && misterXPositionValid) {

			LocalDateTime dateTime = LocalDateTime.now();

			MatchService.getInstance().usePosition(match, position.get().getNumber(), dateTime);
			playerRepository.placePlayer(player, position.get().getNumber(), dateTime);

			if (allPlayersPlacedExcept(matchStatus.getPlayers(), player)) {
				nextRound(matchStatus.getPlayers());
			}

			return true;
		} else {
			return false;
		}
	}

	private boolean allPlayersPlacedExcept(List<Player> players, Player player) {
		return players.stream().filter(p -> !p.getId().equals(player.getId())).allMatch(Player::isPlaced);
	}

	private void nextRound(List<Player> players) {

		final LocalDateTime dateTime = LocalDateTime.now();
		players.forEach(player -> playerRepository.nextRound(player, dateTime));
	}
}
