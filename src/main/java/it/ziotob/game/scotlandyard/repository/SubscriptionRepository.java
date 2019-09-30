package it.ziotob.game.scotlandyard.repository;

import it.ziotob.game.scotlandyard.database.Database;
import it.ziotob.game.scotlandyard.database.Event;
import it.ziotob.game.scotlandyard.model.Subscription;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SubscriptionRepository {

    private final Database database;

    public void createSubscription(LocalDateTime dateTime, String subscriberId) {
        database.putEvent(new Event(subscriberId, Subscription.EVENT_CREATE, null, dateTime), Subscription.GROUP);
    }

    public void subscribeEvent(LocalDateTime dateTime, String subscriberId, String subscribedEvent) {
        database.putEvent(new Event(subscriberId, Subscription.EVENT_SUBSCRIBED_EVENT, subscribedEvent, dateTime), Subscription.GROUP);
    }

    public Optional<Subscription> getSubscription(String subscriberId) {

        return database.getEvents(Subscription.GROUP)
                .collect(Collectors.groupingBy(Event::getId))
                .entrySet().stream()
                .parallel()
                .filter(entry -> subscriberId.equals(entry.getKey()))
                .findFirst()
                .flatMap(subscriptionEvents -> Subscription.buildFromEvents(subscriptionEvents.getValue().stream()));
    }

    public void deleteSubscribedEvent(LocalDateTime dateTime, String subscriberId, String action) {
        database.putEvent(new Event(subscriberId, Subscription.EVENT_UNSUBSCRIBED_EVENT, action, dateTime), Subscription.GROUP);
    }

    public void deleteSubscription(LocalDateTime dateTime, String subscriberId) {
        database.putEvent(new Event(subscriberId, Subscription.EVENT_DELETE_SUBSCRIPTION, null, dateTime), Subscription.GROUP);
    }
}
