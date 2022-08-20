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

import com.clap.model.Comment;
import com.clap.model.Favourite;
import com.clap.model.Music;
import com.clap.model.Role;
import com.clap.model.Search;
import com.clap.model.Tag;
import com.clap.model.User;
import com.clap.model.Validators.ArtisticUploadDataValidator;
import com.clap.model.dataModels.ArtisticContentData;
import com.clap.model.utils.FileUploadUtil;
import com.clap.services.ArtisticContentService;
import com.clap.services.CommentService;
import com.clap.services.FavouriteService;
import com.clap.services.LikeService;
import com.clap.services.MusicService;
import com.clap.services.RoleService;
import com.clap.services.TagService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MusicController {
    private final UserService userService;
    private final MusicService musicService;
    private final ArtisticContentService artisticContentService;
    private final CommentService commentService;
    private final FavouriteService favouriteService;
    private final LikeService likeService;
    private final TagService tagService;
    private final RoleService roleService;
    private final ArtisticUploadDataValidator musicUploadDataValidator;

    @GetMapping("/create_music_content")
    public String createMusicContentView(Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        List<String> allGenres = new ArrayList<String>();
        allGenres.add("Classical");
        allGenres.add("Disco");
        allGenres.add("Flamenco");
        allGenres.add("Pop");
        allGenres.add("Punk");
        allGenres.add("Reggaeton");
        allGenres.add("Rock and Roll");
        allGenres.add("Country");
        allGenres.add("Musical");
        allGenres.add("Folk");
        allGenres.add("Rap");
        allGenres.add("K-pop");
        allGenres.add("Jazz");
        allGenres.add("Trap");
        allGenres.add("Opera");
        allGenres.add("Soul");
        allGenres.add("Electronic");
        allGenres.add("Heavy Metal");

        model.put("musicUploadData", new ArtisticContentData());
        model.put("allGenres", allGenres);
        model.put("search", new Search());
        return "create_music_content.html";
    }

    @PostMapping("/create_music_content")
    public String createGeneralContent(@Valid @ModelAttribute("musicUploadData") ArtisticContentData musicUploadData,
            BindingResult result, Map<String, Object> model, @RequestParam("file") MultipartFile multipartFile)
            throws Exception {
        List<String> allGenres = new ArrayList<String>();
        allGenres.add("Classical");
        allGenres.add("Disco");
        allGenres.add("Flamenco");
        allGenres.add("Pop");
        allGenres.add("Punk");
        allGenres.add("Reggaeton");
        allGenres.add("Rock and Roll");
        allGenres.add("Country");
        allGenres.add("Musical");
        allGenres.add("Folk");
        allGenres.add("Rap");
        allGenres.add("K-pop");
        allGenres.add("Jazz");
        allGenres.add("Trap");
        allGenres.add("Opera");
        allGenres.add("Soul");
        allGenres.add("Electronic");
        allGenres.add("Heavy Metal");
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        model.put("search", new Search());
        model.put("allGenres", allGenres);
        User user = userService.getUserByUsername(username).orElse(null);

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (fileName.equals("")) {
            fileName = "invalidFileName";
        }
        musicUploadData.setContentUrl("/img/user-files/" + user.getId() + "/music/" + fileName);
        musicUploadData.setMultipartFile(multipartFile);
        String uploadDir = "src/main/resources/static/img/user-files/" + user.getId() + "/music";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        musicUploadDataValidator.validate(musicUploadData, result);
        if (result.hasErrors()) {
            return "create_music_content.html";
        }
        Music musicContent = musicService.uploadMusicContent(musicUploadData, user);

        model.put("musicUploadData", musicUploadData);
        return String.format("redirect:/owner/%s/content/%s/role", musicContent.getOwner().getId(),
                musicContent.getId());
    }

    @RequestMapping("/owner/{user_id}/content/{artistic_content_id}/music")
    public String getContentView(@PathVariable String user_id, @PathVariable String artistic_content_id,
            Map<String, Object> model,
            @ModelAttribute ArtisticContentData artisticContentData) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        String contentType = artisticContentService.getTypeById(artistic_content_id);
        if (contentType == null) {
            return "error_404.html";
        }
        if (!contentType.equals("MUSIC")) {
            return "redirect:/choose_category.html";
        }
        User owner = userService.getUserById(user_id).orElse(null);
        Music musicContent = musicService.getMusicContentById(artistic_content_id);
        musicContent.setOwner(owner);
        if (!username.equals(musicContent.getOwner().getUsername())) {
            musicService.updateViewsMusicContent(musicContent);
        }
        artisticContentData.setTitle(musicContent.getTitle());
        artisticContentData.setDescription(musicContent.getDescription());
        artisticContentData.setGenres(musicContent.getGenres());
        artisticContentData.setType(contentType);
        artisticContentData.setContentUrl(musicContent.getContentUrl());
        artisticContentData.setViewCount(0);
        artisticContentData.setOwner(owner);
        artisticContentData.setId(musicContent.getId());

        List<Comment> existingComments = commentService.getsCommentsByContentId(artistic_content_id);
        for(int a = 0;a<existingComments.size();a++){
            Comment comment = existingComments.get(a);
            User user_comment = userService.getUserByCommentId(comment.getId());
            comment.setUser(user_comment);
        }
        
        List<String> videoExtensions = new ArrayList<String>();
        videoExtensions.add(".mp4");
        Boolean isVideo = false;
        String fileUrl = artisticContentService.getContentUrlById(artistic_content_id);
        for (int i = 0; i < videoExtensions.size(); i++) {
            if (fileUrl.contains(videoExtensions.get(i))) {
                isVideo = true;
                break;
            }
        }
        User logged_user = userService.getUserByUsername(username).orElse(null);
        Boolean alreadyFavourite = favouriteService.isAlreadyFavouriteOf(logged_user.getId(), artistic_content_id);
        Boolean alreadyLike = likeService.isAlreadyLikeOf(logged_user.getId(), artistic_content_id);
        Integer numberOfLikes = likeService.getLikeCount(artistic_content_id);

        List<Tag> tagList = tagService.getTagsByContentId(artistic_content_id);
        String tags = "";
        for (int z = 0; z < tagList.size(); z++) {
            if (z == 1) {
                tags = "#" + tagList.get(z).getText();
            } else {
                tags = tags + ",#" + tagList.get(z).getText();
            }
        }

        List<Role> roles = roleService.getRolesByContentId(artistic_content_id);

        model.put("roles",roles);
        model.put("tags", tags);
        model.put("alreadyFavourite", alreadyFavourite);
        model.put("alreadyLike", alreadyLike);
        model.put("numberOfLikes", numberOfLikes);
        model.put("favourite", new Favourite());
        model.put("isVideo", isVideo);
        model.put("comment", new Comment());
        model.put("existingComments", existingComments);
        model.put("artisticContentData", artisticContentData);
        model.put("contentType", contentType);
        model.put("search", new Search());
        return "view_content.html";
    }
}
