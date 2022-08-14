package com.clap.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.clap.model.Cinema;
import com.clap.model.Comment;
import com.clap.model.User;
import com.clap.model.Validators.ArtisticUploadDataValidator;
import com.clap.model.dataModels.ArtisticContentData;
import com.clap.model.utils.FileUploadUtil;
import com.clap.services.ArtisticContentService;
import com.clap.services.CinemaService;
import com.clap.services.CommentService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CinemaController {
    private final UserService userService;
    private final CinemaService cinemaService;
    private final ArtisticContentService artisticContentService;
    private final CommentService commentService;
    private final ArtisticUploadDataValidator cinemaUploadDataValidator;

    @GetMapping("/create_cinema_content")
    public String createCinemaContentView(Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        List<String> genres = new ArrayList<String>();
        genres.add("Action");
        genres.add("Drama");
        genres.add("Mistery");
        genres.add("Adventure");
        genres.add("Documentary");
        genres.add("Romance");
        genres.add("Animation");
        genres.add("Fantasy");
        genres.add("Sci-fi");
        genres.add("Children");
        genres.add("Horror");
        genres.add("Thriller");
        genres.add("Comedy");
        genres.add("Noir");
        genres.add("War");
        genres.add("Crime");
        genres.add("Musical");
        genres.add("Western");

        model.put("cinemaUploadData", new ArtisticContentData());
        model.put("genres", genres);
        return "create_cinema_content.html";
    }

    @PostMapping("/create_cinema_content")
    public String createCinemaContent(@Valid @ModelAttribute("cinemaUploadData") ArtisticContentData cinemaUploadData,
            BindingResult result, Map<String, Object> model, @RequestParam("file") MultipartFile multipartFile,
            @RequestParam(value = "genres", required = false) String genres) throws Exception {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        User user = userService.getUserByUsername(username).orElse(null);

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (fileName.isBlank()) {
            fileName = "invalidFileName";
        }
        cinemaUploadData.setContentUrl("/img/user-files/" + user.getId() + "/cinema/" + fileName);
        cinemaUploadData.setMultipartFile(multipartFile);
        String uploadDir = "src/main/resources/static/img/user-files/" + user.getId() + "/cinema";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        cinemaUploadDataValidator.validate(cinemaUploadData, result);
        if (result.hasErrors()) {
            return "create_cinema_content.html";
        }
        Cinema cinemaContent = cinemaService.uploadCinemaContent(cinemaUploadData, user);

        String g = genres;
        model.put("cinemaUploadData", cinemaUploadData);
        return String.format("redirect:/owner/%s/content/%s/role", cinemaContent.getOwner().getId(),
                cinemaContent.getId());
    }

    @RequestMapping("/owner/{user_id}/content/{artistic_content_id}/cinema")
    public String getCinemaContentView(@PathVariable String user_id, @PathVariable String artistic_content_id,
            Map<String, Object> model,
            @ModelAttribute ArtisticContentData artisticContentData) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        String contentType = artisticContentService.getTypeById(artistic_content_id);
        if (contentType == null) {
            return "redirect:/choose_category.html";
        }
        if (!contentType.equals("CINEMA")) {
            return "redirect:/choose_category.html";
        }
        User owner = userService.getUserById(user_id).orElse(null);
        Cinema cinemaContent = cinemaService.getCinemaContentById(artistic_content_id);
        cinemaContent.setOwner(owner);
        if (!username.equals(cinemaContent.getOwner().getUsername())) {
            cinemaService.updateViewsCinemaContent(cinemaContent);
        }
        artisticContentData.setTitle(cinemaContent.getTitle());
        artisticContentData.setDescription(cinemaContent.getDescription());
        artisticContentData.setType(contentType);
        artisticContentData.setContentUrl(cinemaContent.getContentUrl());
        artisticContentData.setViewCount(0);
        artisticContentData.setOwner(owner);
        artisticContentData.setId(cinemaContent.getId());
        
        List<Comment> existingComments = commentService.getsCommentsByContentId(artistic_content_id);
        
        model.put("comment", new Comment());
        model.put("existingComments", existingComments);
        model.put("artisticContentData", artisticContentData);
        model.put("contentType", contentType);
        return "view_content.html";
    }
}