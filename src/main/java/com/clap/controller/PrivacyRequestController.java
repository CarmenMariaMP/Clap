package com.clap.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clap.model.PrivacyRequest;
import com.clap.model.Search;
import com.clap.model.User;
import com.clap.services.CompanyService;
import com.clap.services.ContentCreatorService;
import com.clap.services.PrivacyRequestService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PrivacyRequestController {
    private final UserService userService;
    private final PrivacyRequestService privacyRequestService;
    private final ContentCreatorService contentCreatorService;
    private final CompanyService companyService;

    @PostMapping("/profile/{userId}/sendPrivacyRequest")
	public String sendPrivacyRequest(@PathVariable String userId,Map<String, Object> model) throws Exception {
		String username_sender = userService.getLoggedUser();
        if (username_sender == null) {
            return "redirect:/login";
        } else {
            User sender = userService.getUserByUsername(username_sender).orElse(null);
            User receiver = userService.getUserById(userId).orElse(null);
            Boolean requestExists = privacyRequestService.requestExists(receiver.getUsername(), username_sender);
            if(requestExists){
                return String.format("redirect:/profile/%s", userId);
            }
            if(!sender.getType().equals("COMPANY")){
                return String.format("redirect:/profile/%s", userId);
            }
            try{
                privacyRequestService.sendPrivacyRequest(receiver, sender);
            }catch(Exception e){
                e.getStackTrace();
                return String.format("redirect:/profile/%s", userId);
            }
            return String.format("redirect:/profile/%s", userId);
        }
	}

    @RequestMapping("/privacyRequests")
	public String privacyRequestStateView(Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        } else {
            String userType = userService.getTypeByUsername(username);
            if(userType.equals("CONTENT_CREATOR")){
                List<PrivacyRequest> privacyRequests = privacyRequestService.getPrivacyRequestsByContentCreatorUsername(username);
                for(int i=0;i<privacyRequests.size();i++){
                    String companyUsername= privacyRequests.get(i).getCompanyUsername();
                    privacyRequests.get(i).setCompany(companyService.getCompanyByUsername(companyUsername));
                }
                Integer pendingRequests = privacyRequestService.getNumberPendingRequestsByContentCreator(username, "PENDING");
                try{
                    model.put("privacyRequests", privacyRequests);
                    model.put("pendingRequests",pendingRequests);
                }catch(Exception e){
                    e.getStackTrace();
                    return String.format("redirect:/choose_category.html");
                }
                return "response_requests.html";
            }else{
                List<PrivacyRequest> privacyRequests = privacyRequestService.getPrivacyRequestsByCompany(username);
                for(int i=0;i<privacyRequests.size();i++){
                    String contentCreatorUsername= privacyRequests.get(i).getContentCreatorUsername();
                    privacyRequests.get(i).setContentCreator(contentCreatorService.getContentCreatorByUsername(contentCreatorUsername));
                }
                model.put("search", new Search());
                model.put("privacyRequests",privacyRequests);
                return "requests_state.html";
            }
        }
    }
    

    @PostMapping("/privacyRequests/{requestId}/acceptPrivacyRequest")
	public String acceptPrivacyRequest(@PathVariable String requestId) throws Exception {
		String username_sender = userService.getLoggedUser();
        if (username_sender == null) {
            return "redirect:/login";
        } else {
            User sender = userService.getUserByUsername(username_sender).orElse(null);
            if(sender.getType().equals("COMPANY")){
                return String.format("redirect:/privacyRequests");
            }
            PrivacyRequest pr = privacyRequestService.getById(requestId).orElse(null);
            //User owner_pr = userService.ge
            try{
                privacyRequestService.acceptPrivacyRequest(sender, requestId);
            }catch(Exception e){
                e.getStackTrace();
                return String.format("redirect:/privacyRequests");
            }
            return String.format("redirect:/privacyRequests");
        }
	}

    @PostMapping("/privacyRequests/{requestId}/declinePrivacyRequest")
	public String declinePrivacyRequest(@PathVariable String requestId) throws Exception {
		String username_sender = userService.getLoggedUser();
        if (username_sender == null) {
            return "redirect:/login";
        } else {
            User sender = userService.getUserByUsername(username_sender).orElse(null);
            try{
                privacyRequestService.declinePrivacyRequest(sender, requestId);
            }catch(Exception e){
                e.getStackTrace();
                return String.format("redirect:/privacyRequests");
            }
            return String.format("redirect:/privacyRequests");
        }
	}
}
