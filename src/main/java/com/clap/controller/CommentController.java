package com.clap.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.clap.model.Comment;
import com.clap.model.CommentResponse;
import com.clap.model.Validators.CommentValidator;
import com.clap.services.ArtisticContentService;
import com.clap.services.CommentResponseService;
import com.clap.services.CommentService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final UserService userService;
    private final CommentService commentService;
    private final CommentResponseService commentResponseService;
    private final ArtisticContentService artisticContentService;
    private final CommentValidator commentValidator;

    @PostMapping("/owner/{user_id}/content/{artistic_content_id}/comment")
    public String addComment(@Valid @ModelAttribute("comment") Comment comment,
            @PathVariable String user_id, @PathVariable String artistic_content_id, Map<String, Object> model,
            BindingResult result) {
        String username = userService.getLoggedUser();
        String contentType = artisticContentService.getTypeById(artistic_content_id);
        if (username == null) {
            return "redirect:/login";
        }
        commentValidator.validate(comment, result);
        if (result.hasErrors()) {
            String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id, contentType);
        }
        try {
            commentService.addComment(comment, artistic_content_id, username);
        } catch (Exception e) {
            String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id, contentType);
        }
        model.put("user_id", user_id);
        model.put("artistic_content_id", artistic_content_id);
        model.put("contentType", contentType);
        return String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id,
                contentType.toLowerCase());
    }

    @PostMapping("/owner/{user_id}/content/{artistic_content_id}/comment/{comment_id}/comment_response")
    public String addComment(@Valid @ModelAttribute("commentResponse") CommentResponse commentResponse,
            @PathVariable String user_id, @PathVariable String artistic_content_id, @PathVariable String comment_id,
            Map<String, Object> model,BindingResult result) {
        String username = userService.getLoggedUser();
        String contentType = artisticContentService.getTypeById(artistic_content_id);
        if (username == null) {
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id, contentType);
        }
        try {
            commentResponseService.addCommentResponse(commentResponse, comment_id, username);
        } catch (Exception e) {
            String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id, contentType);
        }
        model.put("user_id", user_id);
        model.put("artistic_content_id", artistic_content_id);
        model.put("contentType", contentType);
        return String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id,
                contentType.toLowerCase());
    }
}
