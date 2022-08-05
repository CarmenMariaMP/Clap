package com.clap.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.clap.model.User;
import com.clap.services.SubscriptionService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SubscriptionController {
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    @PostMapping("/profile/{userId}/subscription")
	public String subscribeToUser(@PathVariable String userId,Map<String, Object> model) {
		String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        } else {
            User follower = userService.getUserByUsername(username).orElse(null);
            User followed = userService.getUserById(userId).orElse(null);
            Boolean alreadySubscribed = subscriptionService.isAlreadySubscribedTo(followed.getUsername(), follower.getUsername());
            if(alreadySubscribed){
                return String.format("redirect:/profile/%s", userId);
            }
            if(follower.getUsername().equals(followed.getUsername())){
                return String.format("redirect:/profile/%s", userId);
            }
            model.put("alreadySubscribed", alreadySubscribed);
            subscriptionService.subscribeTo(followed, follower);
            return String.format("redirect:/profile/%s", userId);
        }
	}

    @PostMapping("/profile/{userId}/unsubscription")
	public String usubscribeToUser(@PathVariable String userId,Map<String, Object> model) {
		String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        } else {
            User follower = userService.getUserByUsername(username).orElse(null);
            User followed = userService.getUserById(userId).orElse(null);
            Boolean alreadySubscribed = subscriptionService.isAlreadySubscribedTo(followed.getUsername(), follower.getUsername());
            if(!alreadySubscribed){
                return String.format("redirect:/profile/%s", userId);
            }
            if(follower.getUsername().equals(followed.getUsername())){
                return String.format("redirect:/profile/%s", userId);
            }
            subscriptionService.unsubscribeFrom(followed, follower);
            model.put("alreadySubscribed", alreadySubscribed);
            return String.format("redirect:/profile/%s", userId);
        }
	}
}
