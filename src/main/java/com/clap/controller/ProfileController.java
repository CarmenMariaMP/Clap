package com.clap.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clap.model.ArtisticContent;
import com.clap.model.Company;
import com.clap.model.ContentCreator;
import com.clap.model.PrivacyRequest;
import com.clap.model.User;
import com.clap.model.dataModels.UserProfileData;
import com.clap.repository.CompanyRepository;
import com.clap.repository.ContentCreatorRepository;
import com.clap.services.ArtisticContentService;
import com.clap.services.PrivacyRequestService;
import com.clap.services.SubscriptionService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final ContentCreatorRepository contentCreatorRepository;
    private final CompanyRepository companyRepository;
    private final UserService userService;
    private final ArtisticContentService artisticContentService;
    private final SubscriptionService subscriptionService;
    private final PrivacyRequestService privacyRequestService;

    @GetMapping("/profile")
    public String profile() {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        } else {
            String type = userService.getTypeByUsername(username);
            System.out.println("------------------------");
        System.out.println(type);
            if (type.equals("CONTENT_CREATOR")) {
                ContentCreator contentCreator = contentCreatorRepository.getContentCreatorByUsername(username);;
                return String.format("redirect:/profile/%s", contentCreator.getId());
            } else {
                Company company = companyRepository.getCompanyByUsername(username);
                return String.format("redirect:/profile/%s", company.getId());
            }
        }
    }
    public User toUserFromContentCreator(ContentCreator contentCreator) {
        User user = new User();
        user.setUsername(contentCreator.getUsername());
        user.setPassword(contentCreator.getPassword());
        user.setEmail(contentCreator.getEmail());
        user.setPhotoUrl(contentCreator.getPhotoUrl());
        user.setPhone(contentCreator.getPhone());
        return user;
    }

    public User toUserFromCompany(Company company) {
        User user = new User();
        user.setUsername(company.getUsername());
        user.setPassword(company.getPassword());
        user.setEmail(company.getEmail());
        user.setPhotoUrl(company.getPhotoUrl());
        user.setPhone(company.getPhone());
        return user;
    }

    @RequestMapping("/profile/{user_id}")
    public String getProfileView(@PathVariable String user_id, Map<String, Object> model,
            @ModelAttribute UserProfileData userProfileData) {
        String type = userService.getTypeById(user_id);
        if (type == null) {
            return "redirect:/";
        } else {
            String logged_username = userService.getLoggedUser();
            User follower = userService.getUserByUsername(logged_username).orElse(null);
            User followed = userService.getUserById(user_id).orElse(null);
            Boolean alreadySubscribed = subscriptionService.isAlreadySubscribedTo(followed.getUsername(), follower.getUsername());
            Boolean self_profile=false;
            if(follower.getUsername().equals(followed.getUsername())){
                self_profile=true;
            }

            String username = "";
            User owner = new User();
            ContentCreator contentCreator = new ContentCreator();
            Company company = new Company();
           
            if (type.equals("CONTENT_CREATOR")) {
                contentCreator = contentCreatorRepository.getContentCreatorById(user_id);
                userProfileData.setContentCreator(contentCreator);
                username = contentCreator.getUsername();
            } else {
                company = companyRepository.getCompanyById(user_id);
                userProfileData.setCompany(company);
                username = company.getUsername();
            }

            userProfileData.setFollowedCount(subscriptionService.getFolloweds(username));
            userProfileData.setFollowerCount(subscriptionService.getFollowers(username));

            List<ArtisticContent> uploaded_contents = artisticContentService.getContentByOwner(username);
            Integer total_posts = uploaded_contents.size();
            if (type.equals("CONTENT_CREATOR")) {
                for (int i = 0; i < total_posts; i++) {
                    owner = toUserFromContentCreator(contentCreator);
                    uploaded_contents.get(i).setOwner(owner);
                }
            } else {
                for (int i = 0; i < total_posts; i++) {
                    owner = toUserFromCompany(company);
                    uploaded_contents.get(i).setOwner(owner);
                }
            }
            userProfileData.setTotalPosts(total_posts);

            List<ArtisticContent> attached_contents = new ArrayList<ArtisticContent>();

            String privacyRequestState="";
            PrivacyRequest pr = privacyRequestService.getRequestByCreatorAndCompany(followed.getUsername(), logged_username).orElse(null);
            if(pr!=null){
                privacyRequestState = pr.getRequestState();
            }

            model.put("type", type);
            model.put("userProfileData", userProfileData);
            model.put("uploaded_contents", uploaded_contents);
            model.put("attached_contents", attached_contents);
            model.put("alreadySubscribed", alreadySubscribed);
            model.put("self_profile", self_profile);
            model.put("privacyRequestState", privacyRequestState);
        }
        return "profile.html";
    }
}