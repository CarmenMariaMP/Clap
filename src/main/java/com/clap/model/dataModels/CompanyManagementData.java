package com.clap.model.dataModels;

import com.clap.model.Company;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyManagementData {

    String username;

	String email;

	String phone;

	String companyName;

	String taxIdNumber;

	String officeAddress;

	String photoUrl;

	public static CompanyManagementData fromCompany(Company co) {
		CompanyManagementData companyManagementData = new CompanyManagementData();
		companyManagementData.setUsername(co.getUsername());
		companyManagementData.setCompanyName(co.getCompanyName());
		companyManagementData.setEmail(co.getEmail());
		companyManagementData.setPhone(co.getPhone());
		companyManagementData.setTaxIdNumber(co.getTaxIdNumber());
		companyManagementData.setOfficeAddress(co.getOfficeAddress());
		companyManagementData.setPhotoUrl(co.getPhotoUrl());
		return companyManagementData;
	}

	public void updateCompany(Company company) {
		company.setUsername(getUsername());
		company.setCompanyName(getCompanyName());
		company.setEmail(getEmail());
		company.setPhone(getPhone());
		company.setTaxIdNumber(getTaxIdNumber());
		company.setOfficeAddress(getOfficeAddress());
		company.setPhotoUrl(getPhotoUrl());
	}
}
