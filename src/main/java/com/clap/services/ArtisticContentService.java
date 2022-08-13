package com.clap.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.clap.model.ArtisticContent;
import com.clap.repository.ArtisticContentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArtisticContentService {
  private final ArtisticContentRepository artisticContentRepository;

  public List<ArtisticContent> getArtisticContent() {
    return artisticContentRepository.getArtisticContent();
  }

  public String getTypeById(String artistic_content_id) {
    return artisticContentRepository.getTypeById(artistic_content_id);
  }

  public List<ArtisticContent> getContentByOwner(String username) {
    return artisticContentRepository.findByOwner(username);
  }

  public String getIdByTitle(String title) {
    return artisticContentRepository.findIdByTitle(title);
  }

  public Optional<ArtisticContent> getContentById(String id) {
    return artisticContentRepository.findById(id);
  }

  public ArtisticContent getByRoleId(String role_id) {
    return artisticContentRepository.findByRoleId(role_id);
  }

  public void deleteContent(ArtisticContent artisticContent) {
    artisticContentRepository.delete(artisticContent);
  }

}
