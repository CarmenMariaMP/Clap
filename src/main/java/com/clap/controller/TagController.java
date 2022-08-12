package com.clap.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clap.model.Tag;
import com.clap.services.ArtisticContentService;
import com.clap.services.TagService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TagController {
    private final UserService userService;
    private final TagService tagService;
    private final ArtisticContentService artisticContentService;

    @RequestMapping("/owner/{user_id}/content/{artistic_content_id}/tag")
    public String addRolesView(@PathVariable String user_id, @PathVariable String artistic_content_id,
            Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        List<Tag> existingTags = tagService.getTagsByContentIdAndUserId(artistic_content_id, user_id);
        model.put("tag", new Tag());
        model.put("user_id", user_id);
        model.put("artistic_content_id", artistic_content_id);
        model.put("existingTags", existingTags);
        return "tags.html";
    }

    @PostMapping("/owner/{user_id}/content/{artistic_content_id}/tag")
    public String addRoles(@Valid @ModelAttribute("tag") Tag tag,
            @PathVariable String user_id, @PathVariable String artistic_content_id, Map<String, Object> model,
            BindingResult result) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            return "tags.html";
        }
        String contentType = artisticContentService.getTypeById(artistic_content_id);
        try {
            tagService.addTag(tag, artistic_content_id);
        } catch (Exception e) {
            return "tags.html";
        }
        model.put("user_id", user_id);
        model.put("artistic_content_id", artistic_content_id);
        model.put("contentType", contentType);
        return String.format("redirect:/owner/%s/content/%s/tag", user_id, artistic_content_id);
    }
}
