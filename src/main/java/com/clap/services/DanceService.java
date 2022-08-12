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
    private final DanceRepository generalRepository;

    public Dance getDanceContentById(String artistic_content_id) {
		return generalRepository.findDanceContentById(artistic_content_id);
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
        return generalRepository.save(danceContent);

    }
}
