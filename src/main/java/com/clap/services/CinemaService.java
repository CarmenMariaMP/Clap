package com.clap.services;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.clap.model.Cinema;
import com.clap.model.User;
import com.clap.model.dataModels.ArtisticContentData;
import com.clap.repository.CinemaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CinemaService {
    private final CinemaRepository cinemaRepository;

    public Cinema getCinemaContentById(String artistic_content_id) {
        return cinemaRepository.findCinemaContentById(artistic_content_id);
    }

    public Cinema uploadCinemaContent(ArtisticContentData cinemaUploadData, User owner)
            throws Exception {
        Cinema cinemaContent = cinemaUploadData.toCinemaContent();
        cinemaContent.setOwner(owner);
        return upload(cinemaContent);
    }

    private Cinema upload(Cinema cinemaContent) throws Exception {
        cinemaContent.setUploadDate(Date.from(Instant.now()));
        cinemaContent.setType("CINEMA");
        return cinemaRepository.save(cinemaContent);

    }
}
