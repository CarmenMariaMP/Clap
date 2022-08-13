package com.clap.controller;

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

import com.clap.model.Photography;
import com.clap.model.User;
import com.clap.model.Validators.ArtisticUploadDataValidator;
import com.clap.model.dataModels.ArtisticContentData;
import com.clap.model.utils.FileUploadUtil;
import com.clap.services.ArtisticContentService;
import com.clap.services.PhotographyService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PhotographyController {
    private final UserService userService;
    private final PhotographyService photographyService;
    private final ArtisticContentService artisticContentService;
    private final ArtisticUploadDataValidator photographyUploadDataValidator;

    @GetMapping("/create_photography_content")
    public String createPhotographyContentView(Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        model.put("photographyUploadData", new ArtisticContentData());
        return "create_photography_content.html";
    }

    @PostMapping("/create_photography_content")
    public String createPhotographyContent(
            @Valid @ModelAttribute("photographyUploadData") ArtisticContentData photographyUploadData,
            BindingResult result, Map<String, Object> model, @RequestParam("file") MultipartFile multipartFile)
            throws Exception {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        User user = userService.getUserByUsername(username).orElse(null);

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (fileName.isBlank()) {
            fileName = "invalidFileName";
        }
        photographyUploadData.setContentUrl("/img/user-files/" + user.getId() + "/photography/" + fileName);
        photographyUploadData.setMultipartFile(multipartFile);
        String uploadDir = "src/main/resources/static/img/user-files/" + user.getId() + "/photography";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        photographyUploadDataValidator.validate(photographyUploadData, result);
        if (result.hasErrors()) {
            return "create_photography_content.html";
        }
        Photography photographyContent = photographyService.uploadPhotographyContent(photographyUploadData, user);

        model.put("photographyUploadData", photographyUploadData);
        return String.format("redirect:/owner/%s/content/%s/role", photographyContent.getOwner().getId(),
                photographyContent.getId());
    }

    @RequestMapping("/owner/{user_id}/content/{artistic_content_id}/photography")
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
        if (!contentType.equals("PHOTOGRAPHY")) {
            return "redirect:/choose_category.html";
        }
        User owner = userService.getUserById(user_id).orElse(null);
        Photography photographyContent = photographyService.getPhotographyContentById(artistic_content_id);
        photographyContent.setOwner(owner);
        if (!username.equals(photographyContent.getOwner().getUsername())) {
            photographyService.updateViewsPhotographyContent(photographyContent);
        }
        artisticContentData.setTitle(photographyContent.getTitle());
        artisticContentData.setDescription(photographyContent.getDescription());
        artisticContentData.setCamera(photographyContent.getCamera());
        artisticContentData.setPlace(photographyContent.getPlace());
        artisticContentData.setSize(photographyContent.getSize());
        artisticContentData.setTechniques(photographyContent.getTechniques());
        artisticContentData.setType(contentType);
        artisticContentData.setContentUrl(photographyContent.getContentUrl());
        artisticContentData.setViewCount(0);
        artisticContentData.setOwner(owner);
        artisticContentData.setId(photographyContent.getId());

        model.put("artisticContentData", artisticContentData);
        model.put("contentType", contentType);
        return "view_content.html";
    }
}
