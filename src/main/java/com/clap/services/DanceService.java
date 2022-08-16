package com.clap.services;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.clap.model.Dance;
import com.clap.model.User;
import com.clap.model.dataModels.ArtisticContentData;
import com.clap.repository.DanceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DanceService {
    private final DanceRepository danceRepository;

    public Dance getDanceContentById(String artistic_content_id) {
		return danceRepository.findDanceContentById(artistic_content_id);
	}

    public Dance uploadDanceContent(ArtisticContentData danceUploadData, User owner)
            throws Exception {
                Dance danceContent = danceUploadData.toDanceContent();
                danceContent.setOwner(owner);
        return upload(danceContent);
    }

    private Dance upload(Dance danceContent) throws Exception {
        danceContent.setUploadDate(Date.from(Instant.now()));
        danceContent.setType("DANCE");
        danceContent.setViewCount(0);
        return danceRepository.save(danceContent);

    }

    public Dance updateViewsDanceContent(Dance dance) {
        dance.setViewCount(dance.getViewCount()+1);   
        return danceRepository.save(dance);
    }
}
