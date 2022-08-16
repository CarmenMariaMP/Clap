package com.clap.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.clap.model.Validators.CompanyRegisterValidator;
import com.clap.model.Validators.ContentCreatorRegisterValidator;
import com.clap.repository.ArtisticContentRepository;
import com.clap.repository.CompanyRepository;
import com.clap.repository.ContentCreatorRepository;
import com.clap.repository.UserRepository;
import com.clap.services.ArtisticContentService;
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
    ArtisticContentService artisticContentService;

    @Autowired
	ContentCreatorRegisterValidator contentCreatorRegisterValidator;

    @Autowired
    CompanyRegisterValidator companyRegisterValidator;

    @GetMapping("/create_project")
    public String create_project() {
        return "create_project.html";
    }

    @GetMapping("/notifications")
    public String notifications() {
        return "notifications.html";
    }

    @GetMapping("/project_detail")
    public String project_detail() {
        return "project_detail.html";
    }
}
