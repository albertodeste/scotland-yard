package it.ziotob.game.scotlandyard.model;

import it.ziotob.game.scotlandyard.database.Event;
import it.ziotob.game.scotlandyard.service.MatchService;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import static it.ziotob.game.scotlandyard.utils.FormatUtils.DATE_TIME_JSON_FORMATTER;
import static it.ziotob.game.scotlandyard.utils.FormatUtils.formatString;
import static java.util.Objects.nonNull;

@NoArgsConstructor
@Getter
public class Player {

    public static final String GROUP = "player";
    public static final String EVENT_CREATE = "create";
    public static final String EVENT_SET_NAME = "set_name";
    public static final String EVENT_SET_ROLE = "set_role";
    public static final String EVENT_SET_POSITION = "set_position";
    public static final String EVENT_SET_MATCH_ID = "set_match_id";
    public static final String EVENT_MOVE_LOW = "move_low";
    public static final String EVENT_MOVE_MID = "move_mid";
    public static final String EVENT_MOVE_HIGH = "move_high";
    public static final String EVENT_MOVE_MISTER_X = "move_mister_x";
    public static final String EVENT_MOVE_DOUBLE = "move_double";
    public static final String EVENT_NEW_ROUND = "new_round";

    private String id;
    private String name;
    private String role;
    private LocalDateTime createdAt;
    private Long position;
    private String matchId;
    private Integer moveLow = 0;
    private Integer moveMid = 0;
    private Integer moveHigh = 0;
    private Integer moveMisterX = 0;
    private Integer moveDouble = 0;
    private Integer residualRoundMoves = 0;
    private Integer currentRound = 0;

    public static Optional<Player> buildFromEvents(Stream<Event> eventStream) {

        Player response = new Player();

        eventStream.sorted(Comparator.comparing(Event::getDateTime))
                .forEach(response::applyEvent);

        return Optional.of(response).filter(player -> nonNull(player.createdAt));
    }

    public String toJSON() {

        return String.format("{\"id\": \"%s\", \"name\": \"%s\", \"role\": \"%s\", \"created_at\": \"%s\", \"position\": %s}",
                id,
                name,
                role,
                DATE_TIME_JSON_FORMATTER.format(createdAt),
                formatString(position)
        );
    }

    private void applyEvent(Event event) {

        if (EVENT_CREATE.equals(event.getType())) {

            id = event.getId();
            createdAt = event.getDateTime();
        } else if (EVENT_SET_NAME.equals(event.getType())) {
            name = event.getValue();
        } else if (EVENT_SET_ROLE.equals(event.getType())) {
            role = event.getValue();
        } else if (EVENT_SET_POSITION.equals(event.getType()) && MatchService.getInstance().canSetPositionAtInstant(matchId, Long.parseLong(event.getValue()), event.getDateTime())) {
            position = Long.parseLong(event.getValue());
        } else if (EVENT_SET_MATCH_ID.equals(event.getType())) {
            matchId = event.getValue();
        } else if (EVENT_MOVE_LOW.equals(event.getType()) && canMove()) {

            position = Long.parseLong(event.getValue());
            moveLow++;
            residualRoundMoves--;
        } else if (EVENT_MOVE_MID.equals(event.getType()) && canMove()) {

            position = Long.parseLong(event.getValue());
            moveMid++;
            residualRoundMoves--;
        } else if (EVENT_MOVE_HIGH.equals(event.getType()) && canMove()) {

            position = Long.parseLong(event.getValue());
            moveHigh++;
            residualRoundMoves--;
        } else if (EVENT_MOVE_MISTER_X.equals(event.getType()) && canMove()) {

            position = Long.parseLong(event.getValue());
            moveMisterX++;
            residualRoundMoves--;
        } else if (EVENT_MOVE_DOUBLE.equals(event.getType()) && canMove()) {

            residualRoundMoves++;
            moveDouble++;
        } else if (EVENT_NEW_ROUND.equals(event.getType())) {

            residualRoundMoves = 1;
            currentRound++;
        } else {
            throw new RuntimeException("Trying to apply event of type " + event.getType() + " on Player object");
        }
    }

    private boolean canMove() {
        return residualRoundMoves > 0;
    }

    public boolean isMisterX() {
        return "mister_x".equals(role);
    }

    public boolean isDetective() {
        return !isMisterX();
    }

    public boolean isPlaced() {
        return nonNull(position);
    }
}
