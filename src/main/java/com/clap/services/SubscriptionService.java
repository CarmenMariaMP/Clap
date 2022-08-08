package com.clap.services;

import org.springframework.stereotype.Service;

import com.clap.model.Subscription;
import com.clap.model.User;
import com.clap.repository.SubscriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public Integer getFollowers(String username) {
        return subscriptionRepository.followersCount(username);
    }

    public Integer getFolloweds(String username) {
        return subscriptionRepository.followedsCount(username);
    }

    public Boolean isAlreadySubscribedTo(String username_followed, String username_follower) {
        return subscriptionRepository.findByFollowerAndFollowed(username_followed, username_follower).isPresent();
    }

    public void subscribeTo(User followed, User follower) {
        Boolean isAlreadySubscribedTo = isAlreadySubscribedTo(followed.getUsername(), follower.getUsername());
        if (!isAlreadySubscribedTo) {
            Subscription subscription = new Subscription();
            subscription.setFollowed(followed);
            subscription.setFollower(follower);
            subscriptionRepository.save(subscription);
        }
        return;
    }

    public void unsubscribeFrom(User follower, User followed) {
        Subscription subscription = subscriptionRepository.findByFollowerAndFollowed(follower.getUsername(), followed.getUsername()).orElse(null);
        if (subscription!=null) {
            subscriptionRepository.delete(subscription);
        }
        return;
    }
}
