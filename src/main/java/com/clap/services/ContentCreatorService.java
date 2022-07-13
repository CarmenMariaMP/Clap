package com.clap.services;

import org.springframework.stereotype.Service;

import com.clap.model.ContentCreator;
import com.clap.model.DataModels.ContentCreatorRegisterData;
import com.clap.repository.ContentCreatorRepository;

@Service
public class ContentCreatorService {
    UserService userService;
    ContentCreatorRepository contentCreatorRepository;

    public ContentCreator registerContentCreator(ContentCreatorRegisterData contentCreatorRegisterData) throws Exception {
		ContentCreator contentCreator = contentCreatorRegisterData.toContentCreator();
		contentCreator = (ContentCreator) userService.register(contentCreator);

		return contentCreatorRepository.save(contentCreator);
	}
    
}
