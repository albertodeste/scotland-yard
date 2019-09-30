package it.ziotob.game.scotlandyard.model;

import it.ziotob.game.scotlandyard.database.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

@Getter
@RequiredArgsConstructor
public class Subscription {

    public static String GROUP = "Subscription";

    public static String EVENT_CREATE = "create";
    public static String EVENT_SUBSCRIBED_EVENT = "subscribed_event";
    public static final String EVENT_UNSUBSCRIBED_EVENT = "unsubscribed_event";
    public static final String EVENT_DELETE_SUBSCRIPTION = "deleted_subscription";

    private String subscriberId;
    private LocalDateTime createdAt;
    private List<String> subscribedEvents = new LinkedList<>();

    public static Optional<Subscription> buildFromEvents(Stream<Event> eventStream) {

        Subscription response = new Subscription();

        eventStream.sorted(Comparator.comparing(Event::getDateTime))
                .forEach(response::applyEvent);

        return Optional.of(response).filter(subscription -> nonNull(subscription.createdAt));
    }

    private void applyEvent(Event event) {

        if (EVENT_CREATE.equals(event.getType())) {

            createdAt = event.getDateTime();
            subscriberId = event.getId();
        } else if (EVENT_SUBSCRIBED_EVENT.equals(event.getType())) {
            subscribedEvents.add(event.getValue());
        } else if (EVENT_UNSUBSCRIBED_EVENT.equals(event.getType())) {
            subscribedEvents.removeIf(el -> event.getValue().equals(el));
        } else if (EVENT_DELETE_SUBSCRIPTION.equals(event.getType())) {

            subscribedEvents = new LinkedList<>();
            subscriberId = null;
            createdAt = null;
        } else {
            throw new RuntimeException("Trying to apply event of type " + event.getType() + " on Subscription object");
        }
    }
}
