package com.clap.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.clap.model.DataModels.CompanyRegisterData;
import com.clap.model.Validators.CompanyRegisterValidator;
import com.clap.services.CompanyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyRegisterValidator companyRegisterValidator;

    @GetMapping("/register_company.html")
    public String registerCompany(Map<String, Object> model) {

        model.put("companyRegisterData", new CompanyRegisterData());
        return "register_company.html";
    }

    @PostMapping("/register_company.html")
    public String doRegisterCompany(
            @Valid @ModelAttribute("companyRegisterData") CompanyRegisterData companyRegisterData,
            BindingResult result) {
        companyRegisterValidator.validate(companyRegisterData,result);
        if (result.hasErrors()) {
            return "register_company.html";
        }
        try {
            companyService.registerCompany(companyRegisterData);
        } catch (Exception e) {
            return "register_company.html";
        }
        return "redirect:/login.html";
    }
    
}
