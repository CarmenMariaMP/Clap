package com.clap.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.clap.model.ArtisticContent;
import com.clap.model.Like;
import com.clap.model.User;
import com.clap.services.ArtisticContentService;
import com.clap.services.LikeService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LikeController {
    private final UserService userService;
    private final ArtisticContentService artisticContentService;
    private final LikeService likeService;

    @PostMapping("/owner/{user_id}/content/{artistic_content_id}/add_like")
    public String addLike(@Valid @ModelAttribute("like") Like like,
            @PathVariable String user_id, @PathVariable String artistic_content_id, Map<String, Object> model,
            BindingResult result) {
        String username = userService.getLoggedUser();
        String contentType = artisticContentService.getTypeById(artistic_content_id).toLowerCase();
        if (username == null) {
            return "redirect:/login";
        } else {
            User user = userService.getUserByUsername(username).orElse(null);
            ArtisticContent artisticContent = artisticContentService.getContentById(artistic_content_id).orElse(null);
            Boolean alreadyLike = likeService.isAlreadyLikeOf(user.getId(), artistic_content_id);
            if (alreadyLike) {
                String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id, contentType);
            }
            try {
                model.put("user_id", user_id);
                model.put("artistic_content_id", artistic_content_id);
                model.put("contentType", contentType);
                model.put("alreadyLike", alreadyLike);
                likeService.addLike(user, artisticContent);
            } catch (Exception e) {
                e.getStackTrace();
                String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id, contentType);
            }
            return String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id, contentType);
        }
    }

    @PostMapping("/owner/{user_id}/content/{artistic_content_id}/delete_like")
    public String deleteLike(@PathVariable String user_id, @PathVariable String artistic_content_id,
            Map<String, Object> model,
            BindingResult result) {
        String username = userService.getLoggedUser();
        String contentType = artisticContentService.getTypeById(artistic_content_id).toLowerCase();
        if (username == null) {
            return "redirect:/login";
        } else {
            User user = userService.getUserByUsername(username).orElse(null);
            ArtisticContent artisticContent = artisticContentService.getContentById(artistic_content_id).orElse(null);
            Boolean alreadyLike = likeService.isAlreadyLikeOf(user_id, artistic_content_id);
            if (!alreadyLike) {
                String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id, contentType);
            }
            try {
                likeService.deleteFromLike(user.getId(), artisticContent.getId());
            } catch (Exception e) {
                e.getStackTrace();
                String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id, contentType);
            }
            model.put("user_id", user_id);
            model.put("artistic_content_id", artistic_content_id);
            model.put("contentType", contentType);
            model.put("alreadyLike", alreadyLike);
            return String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id, contentType);
        }
    }
}
