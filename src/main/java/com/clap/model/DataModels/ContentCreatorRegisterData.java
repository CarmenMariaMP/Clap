package com.clap.model.DataModels;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;

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
		contentCreator.setPhotoUrl("");
		return contentCreator;
	}

}
