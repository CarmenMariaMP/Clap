package com.clap.model.dataModels;

import com.clap.model.ContentCreator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContentCreatorManagementData {

    String username;

	String email;

	String phone;

	String fullName;

	String country;

	String city;

	String photoUrl;

	String biography;

	public static ContentCreatorManagementData fromContentCreator(ContentCreator cc) {
		ContentCreatorManagementData contentCreatorManagementData = new ContentCreatorManagementData();
		contentCreatorManagementData.setUsername(cc.getUsername());
		contentCreatorManagementData.setFullName(cc.getFullName());
		contentCreatorManagementData.setEmail(cc.getEmail());
		contentCreatorManagementData.setCity(cc.getCity());
		contentCreatorManagementData.setCountry(cc.getCountry());
		contentCreatorManagementData.setPhone(cc.getPhone());
		contentCreatorManagementData.setPhotoUrl(cc.getPhotoUrl());
		contentCreatorManagementData.setBiography(cc.getBiography());
		return contentCreatorManagementData;
	}

	public void updateContentCreator(ContentCreator contentCreator) {
		contentCreator.setUsername(getUsername());
		contentCreator.setFullName(getFullName());
		contentCreator.setEmail(getEmail());
		contentCreator.setCity(getCity());
		contentCreator.setCountry(getCountry());
		contentCreator.setPhone(getPhone());
		contentCreator.setPhotoUrl(getPhotoUrl());
		contentCreator.setBiography(getBiography());
	}
}
