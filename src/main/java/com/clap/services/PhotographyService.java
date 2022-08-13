package com.clap.services;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.clap.model.Photography;
import com.clap.model.User;
import com.clap.model.dataModels.ArtisticContentData;
import com.clap.repository.PhotographyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhotographyService {
    private final PhotographyRepository photographyRepository;

    public Photography getPhotographyContentById(String artistic_content_id) {
		return photographyRepository.findPhotographyContentById(artistic_content_id);
	}

    public Photography uploadPhotographyContent(ArtisticContentData photographyUploadData, User owner)
            throws Exception {
                Photography photographyContent = photographyUploadData.toPhotographyContent();
                photographyContent.setOwner(owner);
        return upload(photographyContent);
    }

    private Photography upload(Photography photographyContent) throws Exception {
        photographyContent.setUploadDate(Date.from(Instant.now()));
        photographyContent.setType("PHOTOGRAPHY");
        photographyContent.setViewCount(0);
        return photographyRepository.save(photographyContent);
    }

    public Photography updateViewsPhotographyContent(Photography photography) {
        photography.setViewCount(photography.getViewCount()+1);   
        return photographyRepository.save(photography);
    }
}
