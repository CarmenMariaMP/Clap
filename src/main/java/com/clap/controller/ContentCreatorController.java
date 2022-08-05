package com.clap.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.clap.model.DataModels.ContentCreatorRegisterData;
import com.clap.model.Validators.ContentCreatorRegisterValidator;
import com.clap.services.ContentCreatorService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ContentCreatorController {
    private final ContentCreatorService contentCreatorService;
    private final ContentCreatorRegisterValidator contentCreatorRegisterValidator;

    @GetMapping("/register_content_creator.html")
    public String registerContentCreator(Map<String, Object> model) {

        model.put("contentCreatorRegisterData", new ContentCreatorRegisterData());
        return "register_content_creator.html";
    }

    @PostMapping("/register_content_creator.html")
    public String doRegisterContentCreator(
            @Valid @ModelAttribute("contentCreatorRegisterData") ContentCreatorRegisterData contentCreatorRegisterData,
            BindingResult result) {
        contentCreatorRegisterValidator.validate(contentCreatorRegisterData,result);
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
}
