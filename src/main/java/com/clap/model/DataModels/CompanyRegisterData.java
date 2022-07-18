package com.clap.model.DataModels;

import javax.validation.constraints.NotEmpty;

import com.clap.model.Company;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyRegisterData {
    // NotNull
	@NotEmpty(message = "Not empty")
	String username;

	// NotNull
	@NotEmpty(message = "Not empty")
	String companyName;

	// NotNull
	@NotEmpty(message = "Not empty")
	String taxIDNumber;

	// NotNull
	@NotEmpty(message = "Not empty")
	String phone;

	// NotNull
	@NotEmpty(message = "Not empty")
	String officeAddress;

	@NotEmpty
	String email;

	// NotNull
	@NotEmpty(message = "Not empty")
	String password;

	// NotNull
	@NotEmpty(message = "Not empty")
	String confirmPassword;

	public Company toCompany() {
		Company company = new Company();
		company.setPhone(getPhone());
		company.setCompanyName(getCompanyName());
		company.setOfficeAddress(getOfficeAddress());
		company.setPassword(getPassword());
		company.setTaxIdNumber(getTaxIDNumber());
		company.setEmail(getEmail());
		company.setUsername(getUsername());
		return company;
	}
}
