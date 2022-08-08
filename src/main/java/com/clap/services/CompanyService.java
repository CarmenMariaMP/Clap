package com.clap.services;

import java.time.Instant;
import java.util.Date;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clap.model.Company;
import com.clap.model.dataModels.CompanyManagementData;
import com.clap.model.dataModels.CompanyRegisterData;
import com.clap.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {
	private final CompanyRepository companyRepository;
	private final PasswordEncoder crypt;

	public Company registerCompany(CompanyRegisterData companyRegisterData)
			throws Exception {
		Company company = companyRegisterData.toCompany();
		return register(company);
	}

	private Company register(Company company) throws Exception {
		company.setType(String.format("COMPANY"));
		company.setPassword(crypt.encode(company.getPassword()));
		company.setCreatedDate(Date.from(Instant.now()));
		company.setPhotoUrl("/img/account.png");
		return companyRepository.save(company);

	}

	public Company getCompanyByUsername(String username) {
		return companyRepository.getCompanyByUsername(username);
	}

	public Company getCompanyId(String userId) {
		return companyRepository.getCompanyById(userId);
	}

	public void updateCompany(CompanyManagementData companyManagementData, Company company){
        companyManagementData.updateCompany(company);
        companyRepository.save(company);
    }
}
