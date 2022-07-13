package com.clap.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.clap.model.DataModels.CompanyRegisterData;
import com.clap.services.CompanyService;
import com.clap.services.UserService;

public class CompanyController {

    @Autowired
	CompanyService companyService;

	@Autowired
	UserService userService;


    @GetMapping("/register_company")
	public String registerCompany(Map<String, Object> model) {
		if (userService.getLoggedUser()!="null") {
			return "redirect:/login";
		}
		model.put("companyRegisterData", new CompanyRegisterData());
		return "register_ompany";
	}

	@PostMapping("/register_company")
	public String doRegisterCompany(@ModelAttribute CompanyRegisterData companyRegisterData, BindingResult result) {
		if (userService.getLoggedUser()!="null") {
			return "redirect:/login";
		}
		/*companyRegisterDataValidator.validate(companyRegisterData, result);*/
		if (result.hasErrors()) {
			return "register_company";
		}

		try {
			companyService.registerCompany(companyRegisterData);
		} catch (Exception e) {
			result.rejectValue("username", "", e.getMessage());
			return "register_company";
		}
		return "redirect:/";
	}
    
}
