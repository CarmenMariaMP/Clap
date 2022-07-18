package com.clap.model.DataModels;

import com.clap.model.ContentCreator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContentCreatorRegisterData {

    String username;

	// NotNull
	String email;

	// NotNull
	String password;

	String phone;

	// NotNull
	String fullName;

	String country;

	String city;


	public ContentCreator toContentCreator() {
		ContentCreator contentCreator = new ContentCreator();
		contentCreator.setUsername(getUsername());
		contentCreator.setFullName(getFullName());
		contentCreator.setEmail(getEmail());
		contentCreator.setCity(getCity());
		contentCreator.setCountry(getCountry());
		contentCreator.setPhone(getPhone());
		contentCreator.setPassword(getPassword());
		return contentCreator;
	}

}
