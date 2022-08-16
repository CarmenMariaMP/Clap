package com.clap.controller;

import java.util.Map;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clap.model.ArtisticContent;
import com.clap.model.Favourite;
import com.clap.model.User;
import com.clap.services.ArtisticContentService;
import com.clap.services.FavouriteService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FavouriteController {
    private final UserService userService;
    private final ArtisticContentService artisticContentService;
    private final FavouriteService favouriteService;

    @RequestMapping("/list_favourites.html")
    public String list_favourites(Map<String, Object> model) {
        String username = userService.getLoggedUser();
		if (username == null) {
			return "redirect:/login";
		}
        List<ArtisticContent> favourites = artisticContentService.getFavouritesContentsByUsername(username);
        for(int i=0; i<favourites.size();i++){
            ArtisticContent artisticContent = favourites.get(i);
            User user = userService.getUserByArtisticContentId(artisticContent.getId());
            artisticContent.setOwner(user);

        }
        model.put("favourites", favourites);
        return "list_favourites.html";
    }

    @PostMapping("/owner/{user_id}/content/{artistic_content_id}/add_favourite")
    public String addFavourite(@Valid @ModelAttribute("favourite") Favourite favourite,
            @PathVariable String user_id, @PathVariable String artistic_content_id, Map<String, Object> model,
            BindingResult result) {
        String username = userService.getLoggedUser();
        String contentType = artisticContentService.getTypeById(artistic_content_id).toLowerCase();
        if (username == null) {
            return "redirect:/login";
        } else {
            User user = userService.getUserByUsername(username).orElse(null);
            ArtisticContent artisticContent = artisticContentService.getContentById(artistic_content_id).orElse(null);
            Boolean alreadyFavourite = favouriteService.isAlreadyFavouriteOf(user.getId(), artistic_content_id);
            if(alreadyFavourite){
                String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id, contentType);
            }
            try{
                model.put("user_id", user_id);
                model.put("artistic_content_id", artistic_content_id);
                model.put("contentType", contentType);
                model.put("alreadyFavourite", alreadyFavourite);
                favouriteService.addFavourite(user, artisticContent);
            }catch(Exception e){
                e.getStackTrace();
                String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id, contentType);
            }
            return String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id, contentType);
        }
	}

    @PostMapping("/owner/{user_id}/content/{artistic_content_id}/delete_favourite")
    public String deleteFavourite(@Valid @ModelAttribute("favourite") Favourite favourite,
            @PathVariable String user_id, @PathVariable String artistic_content_id, Map<String, Object> model,
            BindingResult result) {
        String username = userService.getLoggedUser();
        String contentType = artisticContentService.getTypeById(artistic_content_id).toLowerCase();
        if (username == null) {
            return "redirect:/login";
        } else {
            User user = userService.getUserByUsername(username).orElse(null);
            ArtisticContent artisticContent = artisticContentService.getContentById(artistic_content_id).orElse(null);
            Boolean alreadyFavourite = favouriteService.isAlreadyFavouriteOf(user_id, artistic_content_id);
            if(!alreadyFavourite){
                String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id, contentType);
            }
            try{
                model.put("user_id", user_id);
                model.put("artistic_content_id", artistic_content_id);
                model.put("contentType", contentType);
                model.put("alreadyFavourite", alreadyFavourite);
                favouriteService.deleteFromFavourite(user.getId(), artisticContent.getId());
            }catch(Exception e){
                e.getStackTrace();
                String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id, contentType);
            }
            return String.format("redirect:/owner/%s/content/%s/%s", user_id, artistic_content_id, contentType);
        }
	}
}
