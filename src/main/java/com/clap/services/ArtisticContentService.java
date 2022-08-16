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

  public String getIdByContentUrl(String content_url) {
    return artisticContentRepository.findIdByContentUrl(content_url);
  }

  public Optional<ArtisticContent> getContentById(String id) {
    return artisticContentRepository.findById(id);
  }

  public ArtisticContent getByRoleId(String role_id) {
    return artisticContentRepository.findByRoleId(role_id);
  }

  public ArtisticContent getByCommentId(String comment_id) {
    return artisticContentRepository.findByCommentId(comment_id);
  }

  public String getContentUrlById(String artistic_content_id) {
    return artisticContentRepository.findContentUrlById(artistic_content_id);
  }

  public List<ArtisticContent> getFavouritesContentsByUsername(String username) {
    return artisticContentRepository.findFavouritesContentsByUsername(username);
  }

  public List<ArtisticContent> getContentsByFavouriteId(String favourite_id) {
    return artisticContentRepository.findContentsByFavouriteId(favourite_id);
  }

  public List<ArtisticContent> getContentsByLikeId(String like_id) {
    return artisticContentRepository.findContentsByLikeId(like_id);
  }

  public List<ArtisticContent> getContentsByTagId(String tag_id) {
    return artisticContentRepository.findContentsByTagId(tag_id);
  }


  public void deleteContent(ArtisticContent artisticContent) {
    artisticContentRepository.delete(artisticContent);
  }

}
