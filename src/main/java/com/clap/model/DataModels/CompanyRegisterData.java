package com.clap.model.DataModels;

import javax.validation.constraints.NotEmpty;

import com.clap.model.Company;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyRegisterData {
    // NotNull
	String username;

	// NotNull
	String companyName;

	// NotNull
	String taxIdNumber;

	String phone;

	String officeAddress;

	// NotNull
	String email;

	// NotNull
	String password;

	// NotNull
	String confirmPassword;

	public Company toCompany() {
		Company company = new Company();
		company.setPhone(getPhone());
		company.setCompanyName(getCompanyName());
		company.setOfficeAddress(getOfficeAddress());
		company.setPassword(getPassword());
		company.setTaxIdNumber(getTaxIdNumber());
		company.setEmail(getEmail());
		company.setUsername(getUsername());
		return company;
	}
}
