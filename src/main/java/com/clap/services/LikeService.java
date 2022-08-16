package com.clap.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.clap.model.ArtisticContent;
import com.clap.model.Like;
import com.clap.model.User;
import com.clap.repository.LikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final ArtisticContentService artisticContentService;
    private final UserService userService;

    public Boolean isAlreadyLikeOf(String user_id, String artistic_content_id) {
        return likeRepository.findByUserAndContent(user_id, artistic_content_id).isPresent();
    }

    public Like getLikeByUserId(String user_id) {
        return likeRepository.findByUserId(user_id).orElse(null);
    }

    public Integer getLikeCount(String artistic_content_id) {
        return likeRepository.likeCount(artistic_content_id);
    }

    public void deleteArtisticContentRelationship(String artistic_content_id) {
        likeRepository.deleteArtisticContentRelationship(artistic_content_id);
    }

    public void addLike(User user, ArtisticContent artisticContent) {
        Boolean isAlreadyLikeOf = isAlreadyLikeOf(user.getId(), artisticContent.getId());
        Like like = new Like();
        List<ArtisticContent> artisticContents = new ArrayList<ArtisticContent>();
        if (!isAlreadyLikeOf) {
            if(getLikeByUserId(user.getId())!=null){
                like = getLikeByUserId(user.getId());
                artisticContents = artisticContentService.getContentsByLikeId(like.getId());
                for(int i=0;i<artisticContents.size();i++){
                    User owner = userService.getUserByArtisticContentId(artisticContents.get(i).getId());
                    artisticContents.get(i).setOwner(owner);
                }
            }
            like.setUser(user);
            artisticContents.add(artisticContent);
            like.setArtisticContent(artisticContents);
            likeRepository.save(like);
        }
        return;
    }

    public void deleteFromLike(String user_id, String artistic_content_id) {
        Like like = likeRepository.findByUserAndContent(user_id, artistic_content_id).orElse(null);
        if (like!=null) {
            deleteArtisticContentRelationship(artistic_content_id);
            List<ArtisticContent> artisticContents = artisticContentService.getContentsByLikeId(like.getId());
            if(artisticContents.size()==0){
                likeRepository.delete(like);
            }
        }
        return;
    }
}
