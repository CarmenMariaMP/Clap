package com.clap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InitialController {

	@GetMapping("/")
    public String initial() {
        return "landing_page.html";
    }

	@GetMapping("/register_content_creator")
    public String register_content_creator() {
        return "register_content_creator.html";
    }

	@GetMapping("/register_company")
    public String register_company() {
        return "register_company.html";
    }

	@GetMapping("/login")
    public String login() {
        return "login.html";
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
}
