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
import com.clap.model.User;
import com.clap.model.DataModels.CompanyRegisterData;
import com.clap.model.DataModels.ContentCreatorRegisterData;
import com.clap.model.validator.CompanyRegisterDataValidator;
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

    @Autowired
	CompanyRegisterDataValidator companyRegisterDataValidator;


    @GetMapping("/")
    public String index(Model model) {
        
        List<ArtisticContent> contents = artisticContentRepository.getArtisticContent();
        /* 
        List<String> users = userRepository.getUsers();
        List<String> content_creators = contentCreatorRepository.getContentCreators();
        ;
        cunt();
            System.out.println(views);
            System.out.println(contents.get(i).getTitle()+","+ contents.get(i).getDescription());
            
    }
        System.out.println(users);
        System.out.println(content_creators);
        System.out.println(companies);
        */
        for(int i = 0; i < contents.size();  i++)      {
            User user= contents.get(i).getOwner();
            System.out.println(user);
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
        model.put("contentCeeatorRegisterData", new ContentCreatorRegisterData());
        return "register_content_creator.html";
    }

    @PostMapping("/register_content_creator.html")
    public String doRegisterContentCreator(@ModelAttribute CompanyRegisterData companyRegisterData, BindingResult result) {
        System.out.println("Entra en post");
        if (userService.getLoggedUser() != "null") {
            System.out.println("login");
            return "redirect:/login.html";
        }
        if (result.hasErrors()) {
            return "register_content_creator.html";
        }

        try {
            companyService.registerCompany(companyRegisterData);
            System.out.println(companyRegisterData.getCompanyName());
        } catch (Exception e) {
            result.rejectValue("username", "", e.getMessage());
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
            return "register_company.html";
        }

        try {
            companyService.registerCompany(companyRegisterData);
            System.out.println(companyRegisterData.getCompanyName());
        } catch (Exception e) {
            result.rejectValue("username", "", e.getMessage());
            System.out.println("patata");
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
