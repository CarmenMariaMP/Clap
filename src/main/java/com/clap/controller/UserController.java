package com.clap.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
public class UserController {
    private final ArtisticContentService artisticContentService;
    private final UserService userService;
    private final CommentService commentService;
    private final FavouriteService favouriteService;
    private final LikeService likeService;
    private final TagService tagService;
    private final RoleService roleService;

    @GetMapping("/")
    public String index(Model model) {
        Boolean logged_user = false;
        String username = userService.getLoggedUser();
        if (username != null) {
            logged_user = true;
        }
        List<ArtisticContent> contents = artisticContentService.getArtisticContent();

        for (int i = 0; i < contents.size(); i++) {
            ArtisticContent content = contents.get(i);
            String title = contents.get(i).getTitle();
            userService.setUserArtisticContent(title, content);
        }
        model.addAttribute("contents", contents);
        model.addAttribute("logged_user", logged_user);
        model.addAttribute("search", new Search());
        return "landing_page.html";

    }

    @GetMapping("/no_privileges_view.html")
    public String choose_category() {
        return "no_privileges_view.html";
    }

    @RequestMapping("/login.html")
    public String login() {
        return "login.html";
    }

    @RequestMapping("/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @GetMapping("/account")
    public String getManageUserAccount() {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        User user = userService.getUserByUsername(username).orElse(null);
        if (user.getType().equals("CONTENT_CREATOR")) {
            return "redirect:/account/content_creator";
        } else {
            return "redirect:/account/company";
        }
    }

    @PostMapping("/account/delete")
    public String deleteUserAccount() {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        try {
            commentService.deleteCommentsByUsername(username);
            List<ArtisticContent> artisticContents = artisticContentService.getContentByOwner(username);
            for (int z = 0; z < artisticContents.size(); z++) {
                ArtisticContent artisticContent = artisticContents.get(z);
                String artistic_content_id = artisticContent.getId();

                commentService.deleteCommentsByContentId(artistic_content_id);
                roleService.deleteRolesByContentId(artistic_content_id);

                List<String> users_id = userService.getAllUsersId();
                for (int i = 0; i < users_id.size(); i++) {
                    String id = users_id.get(i);
                    Boolean alreadyFavourite = favouriteService.isAlreadyFavouriteOf(id, artistic_content_id);
                    Boolean alreadyLike = likeService.isAlreadyLikeOf(id, artistic_content_id);
                    if (alreadyFavourite) {
                        favouriteService.deleteFromFavourite(id, artistic_content_id);
                    }
                    if (alreadyLike) {
                        likeService.deleteFromLike(id, artisticContent.getId());
                    }
                }

                List<Tag> allContentTags = tagService.getTagsByContentId(artistic_content_id);
                for (int j = 0; j < allContentTags.size(); j++) {
                    Tag tag = allContentTags.get(j);
                    tagService.deleteTag(tag.getId(), artistic_content_id);
                }

                Path fileToDeletePath = Paths.get("src/main/resources/static/" + artisticContent.getContentUrl());
                Files.delete(fileToDeletePath);
                artisticContentService.deleteContent(artisticContents.get(z));
            }
            userService.deleteAccount(username);
        } catch (Exception e) {
            return "redirect:/account/";
        }
        return "redirect:/logout";
    }
}
