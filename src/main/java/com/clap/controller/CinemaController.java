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
import com.clap.model.Favourite;
import com.clap.model.Search;
import com.clap.model.Tag;
import com.clap.model.User;
import com.clap.model.Validators.ArtisticUploadDataValidator;
import com.clap.model.dataModels.ArtisticContentData;
import com.clap.model.utils.FileUploadUtil;
import com.clap.services.ArtisticContentService;
import com.clap.services.CinemaService;
import com.clap.services.CommentService;
import com.clap.services.FavouriteService;
import com.clap.services.LikeService;
import com.clap.services.TagService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CinemaController {
    private final UserService userService;
    private final CinemaService cinemaService;
    private final ArtisticContentService artisticContentService;
    private final CommentService commentService;
    private final FavouriteService favouriteService;
    private final LikeService likeService;
    private final TagService tagService;
    private final ArtisticUploadDataValidator cinemaUploadDataValidator;

    @GetMapping("/create_cinema_content.html")
    public String createCinemaContentView(Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        List<String> allGenres = new ArrayList<String>();
        allGenres.add("Action");
        allGenres.add("Drama");
        allGenres.add("Mistery");
        allGenres.add("Adventure");
        allGenres.add("Documentary");
        allGenres.add("Romance");
        allGenres.add("Animation");
        allGenres.add("Fantasy");
        allGenres.add("Sci-fi");
        allGenres.add("Children");
        allGenres.add("Horror");
        allGenres.add("Thriller");
        allGenres.add("Comedy");
        allGenres.add("Film Noir");
        allGenres.add("War");
        allGenres.add("Crime");
        allGenres.add("Musical");
        allGenres.add("Western");

        model.put("cinemaUploadData", new ArtisticContentData());
        model.put("allGenres", allGenres);
        model.put("search", new Search());
        return "create_cinema_content.html";
    }

    @PostMapping("/create_cinema_content.html")
    public String createCinemaContent(@Valid @ModelAttribute("cinemaUploadData") ArtisticContentData cinemaUploadData,
            BindingResult result, Map<String, Object> model, @RequestParam("file") MultipartFile multipartFile,
            @RequestParam(value = "genres", required = false) String genres) throws Exception {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        User user = userService.getUserByUsername(username).orElse(null);

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (fileName.equals("")) {
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

        List<String> g = cinemaContent.getGenres();
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
        artisticContentData.setGenres(cinemaContent.getGenres());
        artisticContentData.setContentUrl(cinemaContent.getContentUrl());
        artisticContentData.setViewCount(0);
        artisticContentData.setOwner(owner);
        artisticContentData.setId(cinemaContent.getId());
        
        List<Comment> existingComments = commentService.getsCommentsByContentId(artistic_content_id);
        
        List<String> videoExtensions = new ArrayList<String>();
        videoExtensions.add(".mp4");
        Boolean isVideo = false;
        String fileUrl = artisticContentService.getContentUrlById(artistic_content_id);
        for(int i=0;i<videoExtensions.size();i++){
            if(fileUrl.contains(videoExtensions.get(i))){
                isVideo =true;
                break;
            }
        }
        User logged_user = userService.getUserByUsername(username).orElse(null);
        Boolean alreadyFavourite = favouriteService.isAlreadyFavouriteOf(logged_user.getId(), artistic_content_id);
        Boolean alreadyLike = likeService.isAlreadyLikeOf(logged_user.getId(), artistic_content_id);
        Integer numberOfLikes = likeService.getLikeCount(artistic_content_id);
        
        List<Tag> tagList = tagService.getTagsByContentId(artistic_content_id);
        String tags = "";
        for(int z=0;z<tagList.size();z++){
            if(z==1){
                tags = "#"+tagList.get(z).getText();
            }else{
                tags = tags+",#"+tagList.get(z).getText();
            }
        }

        model.put("tags",tags);
        model.put("search", new Search());
        model.put("alreadyFavourite",alreadyFavourite);
        model.put("alreadyLike",alreadyLike);
        model.put("numberOfLikes",numberOfLikes);
        model.put("favourite", new Favourite());
        model.put("isVideo",isVideo);
        model.put("comment", new Comment());
        model.put("existingComments", existingComments);
        model.put("artisticContentData", artisticContentData);
        model.put("contentType", contentType);
        return "view_content.html";
    }
}
