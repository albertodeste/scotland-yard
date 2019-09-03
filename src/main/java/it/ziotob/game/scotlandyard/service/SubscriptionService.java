package it.ziotob.game.scotlandyard.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

import static java.util.Objects.isNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionService {

    private static SubscriptionService instance;

    public static SubscriptionService getInstance() {

        if (isNull(instance)) {
            instance = new SubscriptionService();
        }

        return instance;
    }

    public String listenNotifications(String subscriberId) {

        //TODO check on database for queue of the given subscriber
        //TODO if nothing is found add callback to subscription event

        return "{'test': 'OK'}";
    }

    public void upsertSubscription(String subscriberId, Collection<String> actions) {

        //TODO create new subscription
        //TODO if already present add specified actions
    }

    public void deleteSubscription(String subscriberId) {
        //TODO completely delete subscriber
    }

    public void deleteActions(String subscriberId, Collection<String> actions) {
        //TODO remove subscribed actions from specified subscriber
    }
}
