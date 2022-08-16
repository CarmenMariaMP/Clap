package com.clap.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.clap.model.ArtisticContent;
import com.clap.model.Tag;
import com.clap.model.User;
import com.clap.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final ArtisticContentService artisticContentService;
    private final UserService userService;

    public Optional<Tag> getTagByText(String text) {
        return tagRepository.findTagByText(text);
    }

    public Optional<Tag> getById(String tag_id) {
        return tagRepository.findById(tag_id);
    }

    public List<Tag> getTagsByContentId(String artistic_content_id) {
        return tagRepository.findTagsByContentId(artistic_content_id);
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAllTags();
    }

    public void addTag(Tag tag, String artistic_content_id) {
        Tag existingTag = getTagByText(tag.getText()).orElse(null);
        ArtisticContent artisticContent = artisticContentService.getContentById(artistic_content_id).orElse(null);
        List<ArtisticContent> artisticContents = new ArrayList<ArtisticContent>();
        if (existingTag != null) {
            tag = existingTag;
            artisticContents = artisticContentService.getContentsByTagId(existingTag.getId());
            for (int i = 0; i < artisticContents.size(); i++) {
                User owner = userService.getUserByArtisticContentId(artisticContents.get(i).getId());
                artisticContents.get(i).setOwner(owner);
            }
        }
        artisticContents.add(artisticContent);
        tag.setArtisticContents(artisticContents);
        tagRepository.save(tag);
    }

    public void deleteTag(Tag tag, String artistic_content_id) {
        Tag existingTag = getTagByText(tag.getText()).orElse(null);
        if (existingTag != null) {
            tagRepository.deleteArtisticContentRelationship(artistic_content_id);
            List<ArtisticContent> artisticContents = artisticContentService.getContentsByLikeId(existingTag.getId());
            if(artisticContents.size()==0){
                tagRepository.delete(existingTag);
            }
        }
    }

    public void deleteArtisticContentRelationship(String artistic_content_id) {
        tagRepository.deleteArtisticContentRelationship(artistic_content_id);
    }
}
