package com.clap.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.clap.model.Company;
import com.clap.model.Validators.CompanyManagementValidator;
import com.clap.model.Validators.CompanyRegisterValidator;
import com.clap.model.dataModels.CompanyManagementData;
import com.clap.model.dataModels.CompanyRegisterData;
import com.clap.services.CompanyService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final UserService userService;
    private final CompanyRegisterValidator companyRegisterValidator;
    private final CompanyManagementValidator companyManagementValidator;

    @GetMapping("/register_company.html")
    public String registerCompany(Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username != null) {
            return "redirect:/login";
        }
        model.put("companyRegisterData", new CompanyRegisterData());
        return "register_company.html";
    }

    @PostMapping("/register_company.html")
    public String doRegisterCompany(
            @Valid @ModelAttribute("companyRegisterData") CompanyRegisterData companyRegisterData,
            BindingResult result) {
        String username = userService.getLoggedUser();
        if (username != null) {
            return "redirect:/login";
        }
        companyRegisterValidator.validate(companyRegisterData, result);
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

    @GetMapping("/account/company")
    public String getManageCompanyAccount(Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login.html";
        }
        Company company = companyService.getCompanyByUsername(username);
        if (!company.getType().equals("COMPANY")) {
            return "redirect:/account";
        }
        CompanyManagementData c = CompanyManagementData.fromCompany(company);
        model.put("companyManagementData",c);
        return "manage_company_account.html";
    }

    @PostMapping("/account/company")
    public String doManageContentCreatorAccount(
            @Valid @ModelAttribute("companyManagementData") CompanyManagementData companyManagementData,
            BindingResult result,Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login.html";
        }
        Company company = companyService.getCompanyByUsername(username);
        if (!company.getType().equals("COMPANY")) {
            return "redirect:/account";
        }
        companyManagementValidator.validate(companyManagementData, result);
        if (result.hasErrors()) {
            return "manage_company_account.html";
        }
        companyService.updateCompany(companyManagementData, company);
        model.put("companyManagementData",companyManagementData);
        if(companyManagementData.getUsername().equals(username)){
            return "redirect:/choose_category.html";
        }
        return "redirect:/logout";
    }

}
