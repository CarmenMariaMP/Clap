package com.clap.controller;

import java.io.IOException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.clap.model.Company;
import com.clap.model.Search;
import com.clap.model.Validators.CompanyManagementValidator;
import com.clap.model.Validators.CompanyRegisterValidator;
import com.clap.model.dataModels.CompanyManagementData;
import com.clap.model.dataModels.CompanyRegisterData;
import com.clap.model.utils.FileUploadUtil;
import com.clap.repository.CompanyRepository;
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
    private final CompanyRepository companyRepository;

    @GetMapping("/register_company.html")
    public String registerCompany(Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username != null) {
            return "redirect:/login";
        }
        model.put("companyRegisterData", new CompanyRegisterData());
        model.put("search", new Search());
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
        model.put("search", new Search());
        return "manage_company_account.html";
    }

    @PostMapping("/account/company")
    public String manageCompanyAccount(
            @Valid @ModelAttribute("contentCreatorManagementData") CompanyManagementData companyManagementData,
            BindingResult result,Map<String, Object> model,@RequestParam("image") MultipartFile multipartFile) throws IOException {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login.html";
        }
        Company company = companyService.getCompanyByUsername(username);
        if (!company.getType().equals("COMPANY")) {
            return "redirect:/account";
        }
        
        String new_fileName ="";
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String png = "png";
        String PNG = "PNG";
        String jpg = "jpg";
        String jpeg = "jpeg";
        if(fileName.contains(png)){
            new_fileName = "profile.png";
        }
        else if(fileName.contains(PNG)){
            new_fileName = "profile.PNG";
        }
        else if(fileName.contains(jpg)){
            new_fileName = "profile.jpg";
        }
        else if(fileName.contains(jpeg)){
            new_fileName = "profile.jpeg";
        }else if(!fileName.equals("")){
            new_fileName="other";
        }
        if (new_fileName==""){
            company.setPhotoUrl(company.getPhotoUrl());
            companyRepository.save(company);
        }else{
            company.setPhotoUrl("/img/user-photos/" + company.getId()+"/profile/"+new_fileName);
            companyRepository.save(company);
            String uploadDir = "src/main/resources/static/img/user-photos/" + company.getId()+"/profile";
            FileUploadUtil.saveFile(uploadDir, new_fileName, multipartFile);
        }
        companyManagementData.setPhotoUrl(company.getPhotoUrl());
        companyManagementValidator.validate(companyManagementData, result);
        if (result.hasErrors()) {
            return "manage_creator_account.html";
        }

        companyService.updateCompany(companyManagementData, company);
        
        model.put("contentCreatorManagementData",companyManagementData);
        model.put("contentCreator",company);
        if(companyManagementData.getUsername().equals(username)){
            return String.format("redirect:/profile/%s", company.getId());
        }
        return "redirect:/logout";
    }

}
