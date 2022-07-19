package com.clap.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.clap.model.ArtisticContent;
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

    @Autowired
    CompanyService companyService;


    @GetMapping("/")
    public String index(Model model) {
        
        List<ArtisticContent> contents = artisticContentRepository.getArtisticContent();

        for(int i = 0; i < contents.size();  i++)      {
            ArtisticContent content = contents.get(i);
            String title= contents.get(i).getTitle();
            userService.setUserArtisticContent(title, content);
        }
        model.addAttribute("contents", contents);
        return "landing_page";
    }

    @GetMapping("/register_content_creator.html")
    public String registerContentCreator(Map<String, Object> model) {
        if (userService.getLoggedUser() != "null") {
            System.out.println("login");
            return "redirect:/login.html";
        }
        model.put("contentCreatorRegisterData", new ContentCreatorRegisterData());
        return "register_content_creator.html";
    }

    @PostMapping("/register_content_creator.html")
    public String doRegisterContentCreator(@ModelAttribute ContentCreatorRegisterData contentCreatorRegisterData, BindingResult result) {
        System.out.println("Entra en post");
        if (userService.getLoggedUser() != "null") {
            System.out.println("login");
            return "redirect:/login.html";
        }
        if (result.hasErrors()) {
            return "register_content_creator.html";
        }

        try {
            contentCreatorService.registerContentCreator(contentCreatorRegisterData);
        } catch (Exception e) {
            result.rejectValue("username", "", "username");
            result.rejectValue("fullName", "", "fullName");
            result.rejectValue("city", "", "city");
            result.rejectValue("phone", "", "phone");
            result.rejectValue("country", "", "country");
            result.rejectValue("email", "","email");
            result.rejectValue("password", "", "password");
            return "register_content_creator.html";
        }
        return "redirect:/login.html";
    }

    @GetMapping("/register_company.html")
    public String registerCompany(Map<String, Object> model) {
        if (userService.getLoggedUser() != "null") {
            System.out.println("login");
            return "redirect:/login.html";
        }
        model.put("companyRegisterData", new CompanyRegisterData());
        return "register_company.html";
    }

    @PostMapping("/register_company.html")
    public String doRegisterCompany(@ModelAttribute CompanyRegisterData companyRegisterData, BindingResult result) {
        System.out.println("Entra en post");
        if (userService.getLoggedUser() != "null") {
            System.out.println("login");
            return "redirect:/login.html";
        }
        if (result.hasErrors()) {
            System.out.println("error");
            return "register_company.html";
        }

        try {
            System.out.println("try");
            companyService.registerCompany(companyRegisterData);
            System.out.println(companyRegisterData.getCompanyName());
        } catch (Exception e) {
            System.out.println("catch");
            result.rejectValue("username", "", "username");
            result.rejectValue("companyName", "", "companyName");
            result.rejectValue("taxIDNumber", "taxIDNumber", e.getMessage());
            result.rejectValue("phone", "phone", e.getMessage());
            result.rejectValue("officeAddress", "officeAddress", e.getMessage());
            result.rejectValue("email", "email", e.getMessage());
            result.rejectValue("password", "password", e.getMessage());
            return "register_company.html";
        }
        return "redirect:/login.html";
    }
    

    @RequestMapping("/login.html")
    public String login() {
        return "login.html";
    }

    @RequestMapping("/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @GetMapping("/choose_category.html")
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
