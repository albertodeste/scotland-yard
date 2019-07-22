package it.ziotob.game.scotlandyard.model;

import it.ziotob.game.scotlandyard.database.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static it.ziotob.game.scotlandyard.utils.FormatUtils.DATE_TIME_JSON_FORMATTER;
import static java.util.Objects.nonNull;

@NoArgsConstructor
@Getter
public class Match {

    public static final String GROUP = "match";
    public static final String EVENT_CREATE = "create";
    public static final String EVENT_ADDED_PLAYER = "added_player";
    public static final String EVENT_START = "start";
    public static final String EVENT_POSITION_GENERATE = "position_generate";
    public static final String EVENT_POSITION_USE = "position_use";
    public static final String EVENT_POSITION_MISTER_X = "position_mister_x";

    private static final Long EXPIRATION_TIMEOUT = 60L;

    private String id;
    private LocalDateTime startedTime;
    private Boolean started = false;
    private List<String> relatedPlayerIds = new LinkedList<>();
    private List<Position> positions = new LinkedList<>();

    public static Optional<Match> buildFromEvents(Stream<Event> eventStream) {

        Match response = new Match();

        eventStream.sorted(Comparator.comparing(Event::getDateTime))
                .forEach(response::applyEvent);

        return Optional.of(response).filter(match -> nonNull(match.startedTime));
    }

    public String toJSON() {

        return String.format("{\"started_time\":\"%s\", \"related_players\":[%s], \"is_started\": %s}",
                DATE_TIME_JSON_FORMATTER.format(startedTime),
                listToString(relatedPlayerIds, id -> "\"" + id + "\""),
                started.toString());
    }

    private <E> String listToString(List<E> list, Function<E, String> fn) {
        return list.stream().map(fn).reduce((a, b) -> a + "," + b).orElse("");
    }

    private void applyEvent(Event event) {

        if (EVENT_CREATE.equals(event.getType())) {

            id = event.getId();
            startedTime = event.getDateTime();
        } else if (EVENT_ADDED_PLAYER.equals(event.getType())) {
            relatedPlayerIds.add(event.getValue());
        } else if (EVENT_START.equals(event.getType())) {
            started = true;
        } else if(EVENT_POSITION_GENERATE.equals(event.getType())) {
            positions.add(new Position(Long.parseLong(event.getValue())));
        } else if (EVENT_POSITION_USE.equals(event.getType())) {
            positions.stream().filter(p -> p.getNumber().toString().equals(event.getValue())).findFirst().ifPresent(Position::setUsed);
        } else if (EVENT_POSITION_MISTER_X.equals(event.getType())) {
            positions.stream().filter(p -> p.getNumber().toString().equals(event.getValue())).findFirst().ifPresent(Position::setMisterX);
        } else {
            throw new RuntimeException("Trying to apply event of type " + event.getType() + " on Match object");
        }
    }
}
