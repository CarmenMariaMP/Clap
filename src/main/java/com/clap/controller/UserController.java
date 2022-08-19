package com.clap.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.clap.model.ArtisticContent;
import com.clap.model.Search;
import com.clap.model.User;
import com.clap.services.ArtisticContentService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final  ArtisticContentService artisticContentService;
    private final UserService userService;

    @GetMapping("/")
    public String index(Model model) {
        Boolean logged_user = false;
        String username = userService.getLoggedUser();
		if (username != null) {
			logged_user=true;
		}
        List<ArtisticContent> contents = artisticContentService.getArtisticContent();

        for (int i = 0; i < contents.size(); i++) {
            ArtisticContent content = contents.get(i);
            String title = contents.get(i).getTitle();
            userService.setUserArtisticContent(title, content);
        }
        model.addAttribute("contents", contents);
        model.addAttribute("logged_user", logged_user);
        model.addAttribute("search", new Search());
        return "landing_page.html";

    }

    @GetMapping("/no_privileges_view.html")
    public String choose_category() {
        return "no_privileges_view.html";
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

    @GetMapping("/account")
	public String getManageUserAccount() {
		String username = userService.getLoggedUser();
		if (username == null) {
			return "redirect:/login";
		}
        User user = userService.getUserByUsername(username).orElse(null);
		if (user.getType().equals("CONTENT_CREATOR")) {
			return "redirect:/account/content_creator";
		} else {
			return "redirect:/account/company";
		}
	}

    
    @PostMapping("/account/delete")
	public String deleteUserAccount() {
		String username = userService.getLoggedUser();
		if (username == null) {
			return "redirect:/login";
		}
        try {
            userService.deleteAccount(username);
        } catch (Exception e) {
            return "redirect:/account/";
        }
        return "redirect:/logout"; 
	}
}
