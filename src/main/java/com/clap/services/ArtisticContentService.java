package com.clap.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clap.model.ArtisticContent;
import com.clap.repository.ArtisticContentRepository;

@Service
public class ArtisticContentService {

    @Autowired
    ArtisticContentRepository artisticContentRepository;
    /* 
    public ArtisticContent getArtitsticContent(ArtisticContent artisticContent){
        Optional<ArtisticContent> contentFound = artisticContentRepository.findContentByTitle(artisticContent.getTitle());
        return null;
    }
    */
    public List<ArtisticContent> getContentByOwner(String username) {
        return artisticContentRepository.findByOwner(username);
    }

    
}
