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
import com.clap.model.PaintingOrSculpture;
import com.clap.model.User;
import com.clap.model.Validators.ArtisticUploadDataValidator;
import com.clap.model.dataModels.ArtisticContentData;
import com.clap.model.utils.FileUploadUtil;
import com.clap.services.ArtisticContentService;
import com.clap.services.CommentService;
import com.clap.services.FavouriteService;
import com.clap.services.LikeService;
import com.clap.services.PaintingOrSculptureService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PaintingOrSculptureController {
    private final UserService userService;
    private final PaintingOrSculptureService paintingOrSculptureService;
    private final ArtisticContentService artisticContentService;
    private final CommentService commentService;
    private final FavouriteService favouriteService;
    private final LikeService likeService;
    private final ArtisticUploadDataValidator paintinOrSculptureUploadDataValidator;

    @GetMapping("/create_painting_or_sculpture_content")
    public String createPaintingOrSculptureContentView(Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        model.put("paintingOrSculptureUploadData", new ArtisticContentData());
        return "create_painting_sculpture_content.html";
    }

    @PostMapping("/create_painting_or_sculpture_content")
    public String createPaintingOrSculptureContent(
            @Valid @ModelAttribute("paintingOrSculptureUploadData") ArtisticContentData paintingOrSculptureUploadData,
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
        paintingOrSculptureUploadData
                .setContentUrl("/img/user-files/" + user.getId() + "/paintingOrSculpture/" + fileName);
        paintingOrSculptureUploadData.setMultipartFile(multipartFile);
        String uploadDir = "src/main/resources/static/img/user-files/" + user.getId() + "/paintingOrSculpture";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        paintinOrSculptureUploadDataValidator.validate(paintingOrSculptureUploadData, result);
        if (result.hasErrors()) {
            return "create_painting_sculpture_content.html";
        }
        PaintingOrSculpture paintingOrSculptureContent = paintingOrSculptureService
                .uploadPaintingOrSculptureContent(paintingOrSculptureUploadData, user);

        model.put("paintingOrSculptureUploadData", paintingOrSculptureUploadData);
        return String.format("redirect:/owner/%s/content/%s/role", paintingOrSculptureContent.getOwner().getId(),
                paintingOrSculptureContent.getId());
    }

    @RequestMapping("/owner/{user_id}/content/{artistic_content_id}/painting_or_sculpture")
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
        if (!contentType.equals("PAINTING_SCULPTURE")) {
            return "redirect:/choose_category.html";
        }
        User owner = userService.getUserById(user_id).orElse(null);
        PaintingOrSculpture paintingOrSculptureContent = paintingOrSculptureService
                .getPaintingOrSculptureContentById(artistic_content_id);
                paintingOrSculptureContent.setOwner(owner);
        if (!username.equals(paintingOrSculptureContent.getOwner().getUsername())) {
            paintingOrSculptureService.updateViewsPaintingOrSculptureContent(paintingOrSculptureContent);
        }
        artisticContentData.setTitle(paintingOrSculptureContent.getTitle());
        artisticContentData.setDescription(paintingOrSculptureContent.getDescription());
        artisticContentData.setMaterials(paintingOrSculptureContent.getMaterials());
        artisticContentData.setPlace(paintingOrSculptureContent.getPlace());
        artisticContentData.setSize(paintingOrSculptureContent.getSize());
        artisticContentData.setTechniques(paintingOrSculptureContent.getTechniques());
        artisticContentData.setType(contentType);
        artisticContentData.setContentUrl(paintingOrSculptureContent.getContentUrl());
        artisticContentData.setViewCount(0);
        artisticContentData.setOwner(owner);
        artisticContentData.setId(paintingOrSculptureContent.getId());

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
