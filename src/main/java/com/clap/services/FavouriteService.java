package com.clap.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.clap.model.ArtisticContent;
import com.clap.model.Favourite;
import com.clap.model.User;
import com.clap.repository.FavouriteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavouriteService {
    private final FavouriteRepository favouriteRepository;
    private final ArtisticContentService artisticContentService;
    private final UserService userService;

    public Boolean isAlreadyFavouriteOf(String user_id, String artistic_content_id) {
        return favouriteRepository.findByUserAndContent(artistic_content_id, user_id).isPresent();
    }

    public Favourite getFavouriteByUserId(String user_id) {
        return favouriteRepository.findByUserId(user_id).orElse(null);
    }

    public void deleteArtisticContentRelationship(String artistic_content_id) {
        favouriteRepository.deleteArtisticContentRelationship(artistic_content_id);
    }

    public void addFavourite(User user, ArtisticContent artisticContent) {
        Boolean isAlreadyFavouriteFrom = isAlreadyFavouriteOf(user.getId(), artisticContent.getId());
        Favourite favourite = new Favourite();
        List<ArtisticContent> artisticContents = new ArrayList<ArtisticContent>();
        if (!isAlreadyFavouriteFrom) {
            if(getFavouriteByUserId(user.getId())!=null){
                favourite = getFavouriteByUserId(user.getId());
                artisticContents = artisticContentService.getContentsByFavouriteId(favourite.getId());
                for(int i=0;i<artisticContents.size();i++){
                    User owner = userService.getUserByArtisticContentId(artisticContents.get(i).getId());
                    artisticContents.get(i).setOwner(owner);
                }
            }
            favourite.setUser(user);
            artisticContents.add(artisticContent);
            favourite.setArtisticContent(artisticContents);
            favouriteRepository.save(favourite);
        }
    }

    public void deleteFromFavourite(String user_id, String artistic_content_id) {
        Favourite favourite = favouriteRepository.findByUserAndContent(artistic_content_id, user_id).orElse(null);
        if (favourite!=null) {
            favouriteRepository.deleteArtisticContentRelationship(artistic_content_id);
            List<ArtisticContent> artisticContents = artisticContentService.getContentsByFavouriteId(favourite.getId());
            if(artisticContents.size()==0){
                favouriteRepository.delete(favourite);
            }
        }
        return;
    }
}
