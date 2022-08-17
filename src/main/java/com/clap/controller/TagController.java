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

import com.clap.model.Search;
import com.clap.model.Tag;
import com.clap.model.Validators.TagValidator;
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
    private final TagValidator tagValidator;

    @RequestMapping("/owner/{user_id}/content/{artistic_content_id}/tag")
    public String addRolesView(@PathVariable String user_id, @PathVariable String artistic_content_id,
            Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        List<Tag> existingTags = tagService.getTagsByContentId(artistic_content_id);
        model.put("tag", new Tag());
        model.put("user_id", user_id);
        model.put("artistic_content_id", artistic_content_id);
        model.put("existingTags", existingTags);
        model.put("search", new Search());
        return "tags.html";
    }

    @PostMapping("/owner/{user_id}/content/{artistic_content_id}/add_tag")
    public String addTag(@Valid @ModelAttribute("tag") Tag tag,
            @PathVariable String user_id, @PathVariable String artistic_content_id, Map<String, Object> model,
            BindingResult result) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        tagValidator.validate(tag, result);
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

    @PostMapping("/owner/{user_id}/content/{artistic_content_id}/delete_tag/{tag_id}")
    public String deleteTag(@PathVariable String user_id, @PathVariable String artistic_content_id,
            @PathVariable String tag_id, Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        String contentType = artisticContentService.getTypeById(artistic_content_id);
        try {
            Tag tag = tagService.getById(tag_id).orElse(null);
            tagService.deleteTag(tag, artistic_content_id);
        } catch (Exception e) {
            return "tags.html";
        }
        model.put("user_id", user_id);
        model.put("artistic_content_id", artistic_content_id);
        model.put("contentType", contentType);
        return String.format("redirect:/owner/%s/content/%s/tag", user_id, artistic_content_id);
    }

}
