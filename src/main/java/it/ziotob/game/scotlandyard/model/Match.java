package it.ziotob.game.scotlandyard.model;

import it.ziotob.game.scotlandyard.database.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

@NoArgsConstructor
@Getter
public class Match {

    public static final String GROUP = "match";
    public static final String EVENT_CREATE = "create";
    public static final String EVENT_ADDED_PLAYER = "added_player";

    private static final Long EXPIRATION_TIMEOUT = 60L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    private String id;
    private LocalDateTime startedTime;
    private List<String> relatedPlayerIds = new LinkedList<>();

    public static Optional<Match> buildFromEvents(Stream<Event> eventStream) {

        Match response = new Match();

        eventStream.sorted(Comparator.comparing(Event::getDateTime))
                .forEach(response::applyEvent);

        return Optional.of(response).filter(match -> nonNull(match.startedTime));
    }

    public String toJSON() {

        return String.format("{\"started_time\":\"%s\", \"is_valid\":%s, \"related_players\":[%s]}",
                FORMATTER.format(startedTime),
                isValid().toString(),
                listToString(relatedPlayerIds, id -> id));
    }

    private <E> String listToString(List<E> list, Function<E, String> fn) {
        return list.stream().map(fn).reduce((a, b) -> a + "," + b).orElse("");
    }

    public Boolean isValid() {
        return LocalDateTime.now().isBefore(startedTime.plusSeconds(EXPIRATION_TIMEOUT)); //TODO check on related events
    }

    private void applyEvent(Event event) {

        if (EVENT_CREATE.equals(event.getType())) {

            id = event.getId();
            startedTime = event.getDateTime();
        } else if (EVENT_ADDED_PLAYER.equals(event.getType())) {
            relatedPlayerIds.add(event.getValue());
        } else {
            throw new RuntimeException("Trying to apply event of type " + event.getType() + " on Match object");
        }
    }
}
