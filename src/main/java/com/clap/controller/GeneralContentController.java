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
import com.clap.model.General;
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
import com.clap.services.GeneralContentService;
import com.clap.services.LikeService;
import com.clap.services.RoleService;
import com.clap.services.TagService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GeneralContentController {
    private final UserService userService;
    private final GeneralContentService generalContentService;
    private final ArtisticContentService artisticContentService;
    private final CommentService commentService;
    private final ArtisticUploadDataValidator generalUploadDataValidator;
    private final FavouriteService favouriteService;
    private final LikeService likeService;
    private final TagService tagService;
    private final RoleService roleService;

    @GetMapping("/create_general_content")
    public String createGeneralContentView(Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        model.put("generalUploadData", new ArtisticContentData());
        model.put("search", new Search());
        return "create_general_content.html";
    }

    @PostMapping("/create_general_content")
    public String createGeneralContent(
            @Valid @ModelAttribute("generalUploadData") ArtisticContentData generalUploadData,
            BindingResult result, Map<String, Object> model, @RequestParam("file") MultipartFile multipartFile)
            throws Exception {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        model.put("search", new Search());
        User user = userService.getUserByUsername(username).orElse(null);

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (fileName.equals("")) {
            fileName = "invalidFileName";
        }
        generalUploadData.setContentUrl("/img/user-files/" + user.getId() + "/general/" + fileName);
        generalUploadData.setMultipartFile(multipartFile);
        String uploadDir = "src/main/resources/static/img/user-files/" + user.getId() + "/general";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);


        generalUploadDataValidator.validate(generalUploadData, result);
        if (result.hasErrors()) {
            return "create_general_content.html";
        }
        General generalContent = generalContentService.uploadGeneralContent(generalUploadData, user);

        model.put("generalUploadData", generalUploadData);
        return String.format("redirect:/owner/%s/content/%s/role", generalContent.getOwner().getId(),
                generalContent.getId());
    }

    @RequestMapping("/owner/{user_id}/content/{artistic_content_id}/general")
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
        if (!contentType.equals("GENERAL")) {
            return "redirect:/choose_category.html";
        }
        User owner = userService.getUserById(user_id).orElse(null);
        General generalContent = generalContentService.getGeneralContentById(artistic_content_id);
        generalContent.setOwner(owner);
        if (!username.equals(generalContent.getOwner().getUsername())) {
            generalContentService.updateViewsGeneralContent(generalContent);
        }
        artisticContentData.setTitle(generalContent.getTitle());
        artisticContentData.setDescription(generalContent.getDescription());
        artisticContentData.setType(contentType);
        artisticContentData.setContentUrl(generalContent.getContentUrl());
        artisticContentData.setViewCount(generalContent.getViewCount());
        artisticContentData.setOwner(owner);
        artisticContentData.setId(generalContent.getId());

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

        List<Role> roles = roleService.getRolesByContentId(artistic_content_id);

        model.put("roles",roles);
        model.put("tags",tags);
        model.put("alreadyFavourite",alreadyFavourite);
        model.put("alreadyLike",alreadyLike);
        model.put("numberOfLikes",numberOfLikes);
        model.put("favourite", new Favourite());
        model.put("isVideo",isVideo);
        model.put("comment", new Comment());
        model.put("existingComments", existingComments);
        model.put("artisticContentData", artisticContentData);
        model.put("contentType", contentType);
        model.put("search", new Search());
        return "view_content.html";
    }
}
