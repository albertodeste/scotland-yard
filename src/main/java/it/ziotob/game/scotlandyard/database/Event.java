package it.ziotob.game.scotlandyard.database;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static it.ziotob.game.scotlandyard.utils.FormatUtils.DATE_TIME_JSON_FORMATTER;

@RequiredArgsConstructor
@Getter
public class Event {

    private final String id;
    private final String type;
    private final String value;
    private final LocalDateTime dateTime;

    public String toJSON() {

        return String.format("{\"id\": \"%s\", \"type\": \"%s\", \"value\": \"%s\", \"dateTime\": \"%s\"}",
                id,
                type,
                value,
                DATE_TIME_JSON_FORMATTER.format(dateTime)
        );
    }
}
