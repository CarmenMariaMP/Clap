package com.clap.services;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.clap.model.PaintingOrSculpture;
import com.clap.model.User;
import com.clap.model.dataModels.ArtisticContentData;
import com.clap.repository.PaintingOrSculptureRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaintingOrSculptureService {
    private final PaintingOrSculptureRepository paintingOrSculptureRepository;
    public PaintingOrSculpture getPaintingOrSculptureContentById(String artistic_content_id) {
		return paintingOrSculptureRepository.findPaintingOrSculptureContentById(artistic_content_id);
	}

    public PaintingOrSculpture uploadPaintingOrSculptureContent(ArtisticContentData paintingOrSculptureUploadData, User owner)
            throws Exception {
                PaintingOrSculpture paintingOrSculptureContent = paintingOrSculptureUploadData.toPaintingOrSculptureUploadDataContent();
                paintingOrSculptureContent.setOwner(owner);
        return upload(paintingOrSculptureContent);
    }

    private PaintingOrSculpture upload(PaintingOrSculpture paintingOrSculpture) throws Exception {
        paintingOrSculpture.setUploadDate(Date.from(Instant.now()));
        paintingOrSculpture.setType("PAINTING_SCULPTURE");
        paintingOrSculpture.setViewCount(0);
        return paintingOrSculptureRepository.save(paintingOrSculpture);

    }

    public PaintingOrSculpture updateViewsPaintingOrSculptureContent(PaintingOrSculpture paintingOrSculpture) {
        paintingOrSculpture.setViewCount(paintingOrSculpture.getViewCount()+1);   
        return paintingOrSculptureRepository.save(paintingOrSculpture);
    }
}
