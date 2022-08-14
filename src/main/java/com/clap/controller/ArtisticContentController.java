package com.clap.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clap.model.ArtisticContent;
import com.clap.services.ArtisticContentService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ArtisticContentController {
    private final UserService userService;
    private final ArtisticContentService artisticContentService;

    @GetMapping("/choose_category.html")
    public String choose_category() {
        return "choose_category.html";
    }

    @RequestMapping("/owner/{user_id}/content/{artistic_content_id}")
    public String contentType(@PathVariable String user_id, @PathVariable String artistic_content_id) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        } else {
            String type = artisticContentService.getTypeById(artistic_content_id);
            if (type.equals("GENERAL")) {
                return String.format("redirect:/owner/%s/content/%s/general", user_id, artistic_content_id);
            } else if (type.equals("PAINTING_SCULPTURE")) {
                return String.format("redirect:/owner/%s/content/%s/painting_or_sculpture", user_id,
                        artistic_content_id);
            } else if (type.equals("PHOTOGRAPHY")) {
                return String.format("redirect:/owner/%s/content/%s/photography", user_id, artistic_content_id);
            } else if (type.equals("CINEMA")) {
                return String.format("redirect:/owner/%s/content/%s/cinema", user_id, artistic_content_id);
            } else if (type.equals("DANCE")) {
                return String.format("redirect:/owner/%s/content/%s/dance", user_id, artistic_content_id);
            } else if (type.equals("MUSIC")) {
                return String.format("redirect:/owner/%s/content/%s/music", user_id, artistic_content_id);
            } else {
                return String.format("redirect:/");
            }
        }
    }

    @PostMapping("/owner/{user_id}/content/{artistic_content_id}/content_delete")
    public String deleteContent(@PathVariable String user_id, @PathVariable String artistic_content_id,
            Map<String, Object> model) throws IOException {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        } else {
            model.put("user_id", user_id);
            model.put("artistic_content_id", artistic_content_id);
            ArtisticContent artisticContent = artisticContentService.getContentById(artistic_content_id).orElse(null);
            Path fileToDeletePath = Paths.get("src/main/resources/static/"+artisticContent.getContentUrl());
            Files.delete(fileToDeletePath);
            artisticContentService.deleteContent(artisticContent);
            return String.format("redirect:/profile/%s", user_id);
        }
    }
}