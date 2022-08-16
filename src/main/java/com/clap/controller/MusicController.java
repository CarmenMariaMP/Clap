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
import com.clap.model.User;
import com.clap.model.Validators.ArtisticUploadDataValidator;
import com.clap.model.dataModels.ArtisticContentData;
import com.clap.model.utils.FileUploadUtil;
import com.clap.services.ArtisticContentService;
import com.clap.services.CommentService;
import com.clap.services.FavouriteService;
import com.clap.services.LikeService;
import com.clap.services.MusicService;
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
    private final ArtisticUploadDataValidator musicUploadDataValidator;

    @GetMapping("/create_music_content")
    public String createMusicContentView(Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        model.put("musicUploadData", new ArtisticContentData());
        return "create_music_content.html";
    }

    @PostMapping("/create_music_content")
    public String createGeneralContent(@Valid @ModelAttribute("musicUploadData") ArtisticContentData musicUploadData,
            BindingResult result, Map<String, Object> model, @RequestParam("file") MultipartFile multipartFile)
            throws Exception {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
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
            return "redirect:/choose_category.html";
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
