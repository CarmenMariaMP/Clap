package com.clap.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clap.model.ArtisticContent;
import com.clap.model.Search;
import com.clap.model.Tag;
import com.clap.model.User;
import com.clap.services.ArtisticContentService;
import com.clap.services.CommentService;
import com.clap.services.FavouriteService;
import com.clap.services.LikeService;
import com.clap.services.RoleService;
import com.clap.services.TagService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ArtisticContentController {
    private final UserService userService;
    private final ArtisticContentService artisticContentService;
    private final CommentService commentService;
    private final FavouriteService favouriteService;
    private final LikeService likeService;
    private final TagService tagService;
    private final RoleService roleService;

    @GetMapping("/choose_category.html")
    public String choose_category(Map<String, Object> model) {
        model.put("search", new Search());
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
            
            commentService.deleteCommentsByContentId(artistic_content_id);
            roleService.deleteRolesByContentId(artistic_content_id);

            List<String> users_id = userService.getAllUsersId();
            for(int i =0; i<users_id.size();i++){
                String id = users_id.get(i);
                Boolean alreadyFavourite = favouriteService.isAlreadyFavouriteOf(id, artistic_content_id);
                Boolean alreadyLike = likeService.isAlreadyLikeOf(id, artistic_content_id);
                if(alreadyFavourite){
                    favouriteService.deleteFromFavourite(id, artistic_content_id);
                }
                if(alreadyLike){
                    likeService.deleteFromLike(id, artisticContent.getId());
                }
            }

            List<Tag> allContentTags = tagService.getTagsByContentId(artistic_content_id);
            for(int j =0; j<allContentTags.size();j++){
                Tag tag = allContentTags.get(j);
                tagService.deleteTag(tag.getId(), artistic_content_id);
            }

            Path fileToDeletePath = Paths.get("src/main/resources/static/" + artisticContent.getContentUrl());
            Files.delete(fileToDeletePath);
            artisticContentService.deleteContent(artisticContent);
            return String.format("redirect:/profile/%s", user_id);
        }
    }

    @PostMapping("/search")
    public String searchContent(@Valid @ModelAttribute("search") Search search, Map<String, Object> model,
            BindingResult result) {
        Boolean logged_user = true;
        String username = userService.getLoggedUser();
        if (username == null) {
            logged_user = false;
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            return "redirect:/choose_category.html";
        }
        List<ArtisticContent> contents = new ArrayList<>();
        String searchInput = search.getInput().toLowerCase();
        List<ArtisticContent> allArtisticContents = artisticContentService.getArtisticContent();
        for (int i = 0; i < allArtisticContents.size(); i++) {
            ArtisticContent artisticContent = allArtisticContents.get(i);
            User owner = userService.getUserByArtisticContentId(artisticContent.getId());
            artisticContent.setOwner(owner);
            String title = artisticContent.getTitle().toLowerCase();
            String description = artisticContent.getDescription().toLowerCase();
            String type = artisticContent.getType().toLowerCase();
            if (title.contains(searchInput) || description.contains(searchInput) || type.contains(searchInput)
                    || owner.getUsername().contains(searchInput)) {
                contents.add(artisticContent);
            }
        }
        model.put("logged_user", logged_user);
        model.put("contents", contents);
        return "landing_page.html";
    }

}
