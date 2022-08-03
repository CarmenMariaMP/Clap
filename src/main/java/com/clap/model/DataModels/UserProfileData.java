package com.clap.model.DataModels;

import lombok.Data;

import com.clap.model.Company;
import com.clap.model.ContentCreator;

@Data
public class UserProfileData {
    ContentCreator contentCreator;

	Company company;
	
	Integer totalPosts;
	
	Integer followerCount;
	
	Integer followedCount;
    
}
