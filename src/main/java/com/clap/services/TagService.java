package com.clap.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clap.model.ArtisticContent;
import com.clap.model.Tag;
import com.clap.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final ArtisticContentService artisticContentService;

    public Tag getTagByText(String text) {
        return tagRepository.findTagByText(text);
    }

    public List<Tag> getTagsByContentId(String artistic_content_id) {
        return tagRepository.findTagsByContentId(artistic_content_id);
    }

    public void addTag(Tag tag, String artistic_content_id){
        Tag existingTag = getTagByText(tag.getText());
        ArtisticContent artisticContent = artisticContentService.getContentById(artistic_content_id).orElse(null);
        if(existingTag==null){
            tag.getArtisticContents().add(artisticContent);
            tagRepository.save(tag);
        }else{
            existingTag.getArtisticContents().add(artisticContent);
            tagRepository.save(existingTag);
        }
    }
}
