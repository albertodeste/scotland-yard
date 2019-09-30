package it.ziotob.game.scotlandyard.service;

import it.ziotob.game.scotlandyard.database.Database;
import it.ziotob.game.scotlandyard.repository.SubscriptionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;

import static java.util.Objects.isNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private static SubscriptionService instance;

    public static SubscriptionService getInstance() {

        if (isNull(instance)) {
            instance = new SubscriptionService(new SubscriptionRepository(Database.getInstance()));
        }

        return instance;
    }

    public String listenNotifications(String subscriberId) {

        //TODO check on database for queue of the given subscriber
        //TODO if nothing is found add callback to subscription event

        return "{'test': 'OK'}";
    }

    public void upsertSubscription(String subscriberId, Collection<String> actions) {

        LocalDateTime dateTime = LocalDateTime.now();

        if (!subscriptionRepository.getSubscription(subscriberId).isPresent()) {
            subscriptionRepository.createSubscription(dateTime, subscriberId);
        }

        actions.forEach(action -> subscriptionRepository.subscribeEvent(dateTime.plusSeconds(1L), subscriberId, action));
    }

    public void deleteSubscription(String subscriberId) {
        subscriptionRepository.deleteSubscription(LocalDateTime.now(), subscriberId);
    }

    public void deleteActions(String subscriberId, Collection<String> actions) {
        actions.forEach(action -> subscriptionRepository.deleteSubscribedEvent(LocalDateTime.now(), subscriberId, action));
    }
}
