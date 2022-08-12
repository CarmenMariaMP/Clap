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

import com.clap.model.Dance;
import com.clap.model.User;
import com.clap.model.Validators.ArtisticUploadDataValidator;
import com.clap.model.dataModels.ArtisticContentData;
import com.clap.model.utils.FileUploadUtil;
import com.clap.services.ArtisticContentService;
import com.clap.services.DanceService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DanceController {
    private final UserService userService;
    private final DanceService danceService;
    private final ArtisticContentService artisticContentService;
    private final ArtisticUploadDataValidator danceUploadDataValidator;
    
    @GetMapping("/create_dance_content")
    public String createDanceContentView(Map<String, Object> model) {
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

        model.put("danceUploadData", new ArtisticContentData());
        model.put("genres",genres);
        return "create_dance_content.html";
    }

    @PostMapping("/create_dance_content")
    public String createDanceContent(@Valid @ModelAttribute("danceUploadData") ArtisticContentData danceUploadData,
    BindingResult result,Map<String, Object> model,@RequestParam("file") MultipartFile multipartFile,@RequestParam(value = "genres", required = false) String genres) throws Exception {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        User user = userService.getUserByUsername(username).orElse(null);

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if(fileName.isBlank()){
            fileName="invalidFileName";
        }
        danceUploadData.setContentUrl("/img/user-files/" + user.getId()+"/dance/"+fileName);
        danceUploadData.setMultipartFile(multipartFile);
        String uploadDir = "src/main/resources/static/img/user-files/" + user.getId()+"/dance";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        danceUploadDataValidator.validate(danceUploadData, result);
        if (result.hasErrors()) {
            return "create_dance_content.html";
        }
        Dance danceContent = danceService.uploadDanceContent(danceUploadData,user);


        String g = genres;
        model.put("danceUploadData", danceUploadData);
        return String.format("redirect:/owner/%s/content/%s/role",danceContent.getOwner().getId(),danceContent.getId());
    }
    

    @RequestMapping("/owner/{user_id}/content/{artistic_content_id}/dance")
    public String getDanceContentView(@PathVariable String user_id,@PathVariable String artistic_content_id, Map<String, Object> model,
            @ModelAttribute ArtisticContentData artisticContentData) {
        String contentType = artisticContentService.getTypeById(artistic_content_id);
        if (contentType == null) {
            return "redirect:/choose_category.html";
        } 
        if (!contentType.equals("DANCE")) {
            return "redirect:/choose_category.html";
        } 
        User owner = userService.getUserById(user_id).orElse(null);
        Dance danceContent = danceService.getDanceContentById(artistic_content_id);
        artisticContentData.setTitle(danceContent.getTitle());
        artisticContentData.setDescription(danceContent.getDescription());
        artisticContentData.setMusic(danceContent.getMusic());
        artisticContentData.setType(contentType);
        artisticContentData.setContentUrl(danceContent.getContentUrl());
        artisticContentData.setViewCount(0);
        artisticContentData.setOwner(owner);
        artisticContentData.setId(danceContent.getId());

        model.put("artisticContentData", artisticContentData);
        model.put("contentType",contentType);
        return "view_content.html";
    }
}
