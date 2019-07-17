package it.ziotob.game.scotlandyard.model;

import it.ziotob.game.scotlandyard.database.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import static it.ziotob.game.scotlandyard.utils.FormatUtils.DATE_TIME_JSON_FORMATTER;
import static java.util.Objects.nonNull;

@NoArgsConstructor
@Getter
public class Player {

    public static final String GROUP = "player";
    public static final String EVENT_CREATE = "create";
    public static final String EVENT_SET_NAME = "set_name";
    public static final String EVENT_SET_ROLE = "set_role";

    private String id;
    private String name;
    private String role;
    private LocalDateTime createdAt;

    public static Optional<Player> buildFromEvents(Stream<Event> eventStream) {

        Player response = new Player();

        eventStream.sorted(Comparator.comparing(Event::getDateTime))
                .forEach(response::applyEvent);

        return Optional.of(response).filter(player -> nonNull(player.createdAt));
    }

    public String toJSON() {

        return String.format("{\"id\": \"%s\", \"name\": \"%s\", \"role\": %s, \"created_at\": \"%s\"}",
                id,
                name,
                role,
                DATE_TIME_JSON_FORMATTER.format(createdAt)
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
        } else {
            throw new RuntimeException("Trying to apply event of type " + event.getType() + " on Player object");
        }
    }
}
