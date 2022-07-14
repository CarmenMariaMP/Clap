package com.clap.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.clap.model.ArtisticContent;
import com.clap.repository.ArtisticContentRepository;

public class ArtisticContentService {

    @Autowired
    ArtisticContentRepository artisticContentRepository;

    public ArtisticContent getArtitsticContent(ArtisticContent artisticContent){
        Optional<ArtisticContent> contentFound = artisticContentRepository.findContentByTitle(artisticContent.getTitle());
        return null;
    }
    
}
