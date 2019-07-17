package it.ziotob.game.scotlandyard.database;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class Event {

    private final String id;
    private final String type;
    private final String value;
    private final LocalDateTime dateTime;
}
