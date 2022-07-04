package com.clap.services;

import com.clap.model.Company;
import com.clap.model.DataModels.CompanyRegisterData;
import com.clap.repository.CompanyRepository;

public class CompanyService {
    CompanyRepository companyRepository;
	UserService userService;

	public CompanyService(CompanyRepository companyRepository, UserService userService) {
		this.companyRepository = companyRepository;
		this.userService = userService;
	}

    public Company registerCompany(CompanyRegisterData companyRegisterData) throws Exception {
		Company company = companyRegisterData.toCompany();
		company = (Company) userService.register(company);

		return companyRepository.save(company);
	}
    
}
