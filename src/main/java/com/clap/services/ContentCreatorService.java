package com.clap.services;

import java.time.Instant;
import java.util.Date;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clap.model.ContentCreator;
import com.clap.model.DataModels.ContentCreatorRegisterData;
import com.clap.repository.ContentCreatorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContentCreatorService {
    private final ContentCreatorRepository contentCreatorRepository;
    private final PasswordEncoder crypt;

    public ContentCreator registerContentCreator(ContentCreatorRegisterData contentCreatorRegisterData)
            throws Exception {
        ContentCreator contentCreator = contentCreatorRegisterData.toContentCreator();
        return register(contentCreator);
    }

    private ContentCreator register(ContentCreator contentCreator) throws Exception {
        contentCreator.setType(String.format("CONTENT_CREATOR"));
        contentCreator.setPassword(crypt.encode(contentCreator.getPassword()));
        contentCreator.setCreatedDate(Date.from(Instant.now()));
        return contentCreatorRepository.save(contentCreator);

    }

    public ContentCreator getContentCreatorByUsername(String username) {
        return contentCreatorRepository.getContentCreatorByUsername(username);
    }

    public ContentCreator getContentCreatorById(String userId) {
        return contentCreatorRepository.getContentCreatorById(userId);
    }
}
