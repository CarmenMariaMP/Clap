package com.clap.services;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.clap.model.General;
import com.clap.model.User;
import com.clap.model.dataModels.ArtisticContentData;
import com.clap.repository.GeneralRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneralContentService {
    private final GeneralRepository generalRepository;

    public General getGeneralContentById(String artistic_content_id) {
		return generalRepository.findGeneralContentById(artistic_content_id);
	}

    public void save(General general) {
		generalRepository.save(general);
	}

    public General uploadGeneralContent(ArtisticContentData generalUploadData, User owner)
            throws Exception {
                General generalContent = generalUploadData.toGeneralContent();
                generalContent.setOwner(owner);
        return upload(generalContent);
    }

    public General updateViewsGeneralContent(General general) {
        general.setViewCount(general.getViewCount()+1);   
        return generalRepository.save(general);
    }

    private General upload(General generalContent) throws Exception {
        generalContent.setUploadDate(Date.from(Instant.now()));
        generalContent.setType("GENERAL");
        generalContent.setViewCount(0);
        return generalRepository.save(generalContent);

    }
}
