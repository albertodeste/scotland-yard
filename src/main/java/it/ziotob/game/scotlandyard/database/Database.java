package it.ziotob.game.scotlandyard.database;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

public class Database {

    private final Map<String, List<Event>> events;

    private static Database instance;

    private Database() {
        events = new HashMap<>();
    }

    public static Database getInstance() {

        if (isNull(instance)) {
            instance = new Database();
        }

        return instance;
    }

    public Stream<Event> getEvents(String group) {
        return Optional.ofNullable(events.get(group)).map(Collection::stream).orElse(Stream.empty());
    }

    public void putEvent(Event event, String group) {

        if (!events.containsKey(group)) {
            events.put(group, new LinkedList<>());
        }
        events.get(group).add(event);
    }
}
