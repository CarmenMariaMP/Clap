package com.clap.controller;

import java.io.IOException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.clap.model.ContentCreator;
import com.clap.model.Search;
import com.clap.model.Validators.ContentCreatorManagementValidator;
import com.clap.model.Validators.ContentCreatorRegisterValidator;
import com.clap.model.dataModels.ContentCreatorManagementData;
import com.clap.model.dataModels.ContentCreatorRegisterData;
import com.clap.model.utils.FileUploadUtil;
import com.clap.repository.ContentCreatorRepository;
import com.clap.services.ContentCreatorService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ContentCreatorController {
    private final ContentCreatorRepository contentCreatorRepository;
    private final UserService userService;
    private final ContentCreatorService contentCreatorService;
    private final ContentCreatorRegisterValidator contentCreatorRegisterValidator;
    private final ContentCreatorManagementValidator contentCreatorManagementValidator;

    @GetMapping("/register_content_creator.html")
    public String registerContentCreator(Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username != null) {
            return "redirect:/login";
        }
        model.put("contentCreatorRegisterData", new ContentCreatorRegisterData());
        model.put("search", new Search());
        return "register_content_creator.html";
    }

    @PostMapping("/register_content_creator.html")
    public String doRegisterContentCreator(
            @Valid @ModelAttribute("contentCreatorRegisterData") ContentCreatorRegisterData contentCreatorRegisterData,
            BindingResult result) {
        String username = userService.getLoggedUser();
        if (username != null) {
            return "redirect:/login";
        }
        contentCreatorRegisterValidator.validate(contentCreatorRegisterData, result);
        if (result.hasErrors()) {
            return "register_content_creator.html";
        }
        try {
            contentCreatorService.registerContentCreator(contentCreatorRegisterData);
        } catch (Exception e) {
            return "register_content_creator.html";
        }
        return "redirect:/login.html";
    }

    @GetMapping("/account/content_creator")
    public String getManageContentCreatorAccount(Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login.html";
        }
        ContentCreator contentCreator = contentCreatorService.getContentCreatorByUsername(username);
        if (!contentCreator.getType().equals("CONTENT_CREATOR")) {
            return "redirect:/account";
        }
        ContentCreatorManagementData c = ContentCreatorManagementData.fromContentCreator(contentCreator);
        model.put("contentCreatorManagementData",c);
        model.put("photoUrl",contentCreator.getPhotoUrl());
        model.put("search", new Search());
        return "manage_creator_account.html";
    }

    @PostMapping("/account/content_creator")
    public String manageContentCreatorAccount(
            @Valid @ModelAttribute("contentCreatorManagementData") ContentCreatorManagementData contentCreatorManagementData,
            BindingResult result,Map<String, Object> model,@RequestParam("image") MultipartFile multipartFile) throws IOException {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login.html";
        }
        ContentCreator contentCreator = contentCreatorService.getContentCreatorByUsername(username);
        if (!contentCreator.getType().equals("CONTENT_CREATOR")) {
            return "redirect:/account";
        }
        
        String new_fileName ="";
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String png = ".png";
        String PNG = ".PNG";
        String jpg = ".jpg";
        String jpeg = ".jpeg";
        if(fileName.contains(png)){
            new_fileName = "profile.png";
        }
        else if(fileName.contains(PNG)){
            new_fileName = "profile.PNG";
        }
        else if(fileName.contains(jpg)){
            new_fileName = "profile.jpg";
        }
        else if(fileName.contains(jpeg)){
            new_fileName = "profile.jpeg";
        }else if(!fileName.equals("")){
            new_fileName="other";
        }
        if (new_fileName==""){
            contentCreator.setPhotoUrl(contentCreator.getPhotoUrl());
            contentCreatorRepository.save(contentCreator);
        }else{
            contentCreator.setPhotoUrl("/img/user-photos/" + contentCreator.getId()+"/profile/"+new_fileName);
            contentCreatorRepository.save(contentCreator);
            String uploadDir = "src/main/resources/static/img/user-photos/" + contentCreator.getId()+"/profile";
            FileUploadUtil.saveFile(uploadDir, new_fileName, multipartFile);
        }
        contentCreatorManagementData.setPhotoUrl(contentCreator.getPhotoUrl());
        contentCreatorManagementValidator.validate(contentCreatorManagementData, result);
        if (result.hasErrors()) {
            return "manage_creator_account.html";
        }

        contentCreatorService.updateContentCreator(contentCreatorManagementData, contentCreator);
        
        model.put("contentCreatorManagementData",contentCreatorManagementData);
        model.put("contentCreator",contentCreator);
        if(contentCreatorManagementData.getUsername().equals(username)){
            return String.format("redirect:/profile/%s", contentCreator.getId());
        }
        return "redirect:/logout";
    }
}
