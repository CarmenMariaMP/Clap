package com.clap.model.dataModels;

import java.time.Instant;
import java.util.Date;

import com.clap.model.ContentCreator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContentCreatorRegisterData {

	String id;

	// NotNull
    String type;

	// NotNull
    String username;

	// NotNull
	String email;

	// NotNull
	String password;

	String confirmPassword;

	String phone;

	// NotNull
	String fullName;

	String country;

	String city;

	String photoUrl;


	public ContentCreator toContentCreator() {
		ContentCreator contentCreator = new ContentCreator();
		contentCreator.setType(getType());
		contentCreator.setCreatedDate(Date.from(Instant.now()));
		contentCreator.setUsername(getUsername());
		contentCreator.setFullName(getFullName());
		contentCreator.setEmail(getEmail());
		contentCreator.setCity(getCity());
		contentCreator.setCountry(getCountry());
		contentCreator.setPhone(getPhone());
		contentCreator.setPassword(getPassword());
		contentCreator.setPhotoUrl(getPhotoUrl());
		return contentCreator;
	}
}
