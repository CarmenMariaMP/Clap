package com.clap.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.clap.model.DataModels.CompanyRegisterData;
import com.clap.model.DataModels.ContentCreatorRegisterData;
import com.clap.repository.ArtisticContentRepository;
import com.clap.repository.CompanyRepository;
import com.clap.repository.ContentCreatorRepository;
import com.clap.repository.UserRepository;
import com.clap.services.CompanyService;
import com.clap.services.ContentCreatorService;
import com.clap.services.UserService;

@Controller
public class InitialController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContentCreatorRepository contentCreatorRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    ArtisticContentRepository artisticContentRepository;

    @Autowired
    ContentCreatorService contentCreatorService;

    @Autowired
    UserService userService;

    CompanyService companyService;

    @GetMapping("/")
    public String initial() {
        List<String> content = artisticContentRepository.getArtisticContent();
        List<String> users = userRepository.getUsers();
        List<String> content_creators = contentCreatorRepository.getContentCreators();
        ;
        List<String> companies = companyRepository.getCompanies();
        System.out.println(content);
        System.out.println(users);
        System.out.println(content_creators);
        System.out.println(companies);
        return "landing_page.html";
    }

    @GetMapping("/register_content_creator")
    public String registerContentCreator(Map<String, Object> model) {
        if (userService.getLoggedUser() != "null") {
            return "redirect:/login";
        }
        model.put("contentCreatorRegisterData", new ContentCreatorRegisterData());
        return "register_content_creator";
    }

    @PostMapping("/register_content_creator")
    public String doRegisterContentCreator(@ModelAttribute ContentCreatorRegisterData contentCreatorRegisterData,
            BindingResult result) {
        if (userService.getLoggedUser() != "null") {
            return "redirect:/login";
        }
        /* companyRegisterDataValidator.validate(companyRegisterData, result); */
        if (result.hasErrors()) {
            return "register_content_creator";
        }

        try {
            contentCreatorService.registerContentCreator(contentCreatorRegisterData);
        } catch (Exception e) {
            result.rejectValue("username", "", e.getMessage());
            return "register_content_creator";
        }
        return "redirect:/login";
    }

    @GetMapping("/register_company")
    public String registerCompany(Map<String, Object> model) {
        if (userService.getLoggedUser() != "null") {
            return "redirect:/login";
        }
        model.put("companyRegisterData", new CompanyRegisterData());
        return "register_company";
    }

    @PostMapping("/register_company")
    public String doRegisterCompany(@ModelAttribute CompanyRegisterData companyRegisterData, BindingResult result) {
        if (userService.getLoggedUser() != "null") {
            return "redirect:/login";
        }
        /* companyRegisterDataValidator.validate(companyRegisterData, result); */
        if (result.hasErrors()) {
            return "register_company";
        }

        try {
            companyService.registerCompany(companyRegisterData);
        } catch (Exception e) {
            result.rejectValue("username", "", e.getMessage());
            return "register_company";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLogin(@ModelAttribute Credentials credentials, Map<String, Object> model,
            BindingResult bindingResult) {
        if (userService.getLoggedUser() != "null") {
            return "redirect:/";
        }

        model.put("credentials", new Credentials());
        return "login";
    }

    @PostMapping("/login/success")
    public String redirectLoginSuccess() {
        return "redirect:/";
    }

    @GetMapping("/choose_category")
    public String choose_category() {
        return "choose_category.html";
    }

    @GetMapping("/create_music_content")
    public String create_music_content() {
        return "create_music_content.html";
    }

    @GetMapping("/create_cinema_content")
    public String create_cinema_content() {
        return "create_cinema_content.html";
    }

    @GetMapping("/create_dance_content")
    public String create_dance_content() {
        return "create_dance_content.html";
    }

    @GetMapping("/create_photography_content")
    public String create_photography_content() {
        return "create_photography_content.html";
    }

    @GetMapping("/create_painting_sculpture_content")
    public String create_painting_sculpture_content() {
        return "create_painting_sculpture_content.html";
    }

    @GetMapping("/create_general_content")
    public String create_general_content() {
        return "create_general_content.html";
    }

    @GetMapping("/create_project")
    public String create_project() {
        return "create_project.html";
    }

    @GetMapping("/list_favourites")
    public String list_favourites() {
        return "list_favourites.html";
    }

    @GetMapping("/manage_creator_account")
    public String manage_creator_account() {
        return "manage_creator_account.html";
    }

    @GetMapping("/manage_company_account")
    public String manage_company_account() {
        return "manage_company_account.html";
    }

    @GetMapping("/notifications")
    public String notifications() {
        return "notifications.html";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile.html";
    }

    @GetMapping("/project_detail")
    public String project_detail() {
        return "project_detail.html";
    }

    @GetMapping("/view_content")
    public String view_content() {
        return "view_content.html";
    }
}
