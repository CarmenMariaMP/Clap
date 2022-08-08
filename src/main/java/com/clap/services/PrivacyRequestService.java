package com.clap.services;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.neo4j.ogm.exception.core.NotFoundException;
import org.springframework.stereotype.Service;

import com.clap.exceptions.DuplicateActionException;
import com.clap.exceptions.NotAuthorizedExcepcion;
import com.clap.model.Company;
import com.clap.model.ContentCreator;
import com.clap.model.PrivacyRequest;
import com.clap.model.User;
import com.clap.repository.PrivacyRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrivacyRequestService {
    private final PrivacyRequestRepository privacyRequestRepository;
    private final ContentCreatorService contentCreatorService;
    private final CompanyService companyService;

    public Boolean requestExists(String userContentCreator, String userCompany) {
        return privacyRequestRepository.findByCreatorAndCompany(userContentCreator, userCompany).isPresent();
    }

    public Optional<PrivacyRequest> getRequestByCreatorAndCompany(String userContentCreator, String userCompany) {
        return privacyRequestRepository.findByCreatorAndCompany(userContentCreator, userCompany);
    }

    public Optional<PrivacyRequest> getById(String requestId) {
        return privacyRequestRepository.findById(requestId);
    }

    public List<PrivacyRequest> getPrivacyRequestsByContentCreatorUsernme(String username) {
        return privacyRequestRepository.findPrivacyRequestsByContentCreatorUsernme(username);
    }

    public List<PrivacyRequest> getPrivacyRequestsByCompany(String username) {
        return privacyRequestRepository.findPrivacyRequestsByCompany(username);
    }

    public Integer getNumberPendingRequestsByContentCreator(String username,String request_state) {
        return privacyRequestRepository.findNumberPendingRequestsByContentCreator(username,request_state);
    }


    public void sendPrivacyRequest(User receiver, User sender) throws Exception {
        String type = receiver.getType();
        System.out.println(type);
        if (!receiver.getType().equals("CONTENT_CREATOR")) {
            throw new NotAuthorizedExcepcion("You must be a content creator to make this action.");
        }
        if (!sender.getType().equals("COMPANY")) {
            throw new NotAuthorizedExcepcion("You must be a company to make this action.");
        }
        Boolean requestExists = requestExists(receiver.getUsername(), sender.getUsername());
        if (requestExists) {
            throw new DuplicateActionException("This action has already been carried out.");
        }
        ContentCreator contentCreator = contentCreatorService
                .getContentCreatorByUsername(receiver.getUsername());
        Company company = companyService.getCompanyByUsername(sender.getUsername());
        PrivacyRequest privacyRequest = new PrivacyRequest();
        privacyRequest.setContentCreator(contentCreator);
        privacyRequest.setCompany(company);
        privacyRequest.setContentCreatorUsername(contentCreator.getUsername());
        privacyRequest.setCompanyUsername(company.getUsername());
        privacyRequest.setRequestDate(Date.from(Instant.now()));
        privacyRequest.setRequestState("PENDING");
        privacyRequestRepository.save(privacyRequest);
        return;
    }

    public void acceptPrivacyRequest(User sender, String requestId) throws Exception {
        if (!sender.getType().equals("CONTENT_CREATOR")) {
            throw new NotAuthorizedExcepcion("You must be a content creator to make this action.");
        }
        PrivacyRequest privacyRequest = getById(requestId).orElse(null);
        if (privacyRequest == null) {
            throw new NotFoundException("Privacy request not found");
        }
        if (!sender.getUsername().equals(privacyRequest.getContentCreatorUsername())) {
            throw new NotAuthorizedExcepcion("This privacy request does not belong to you");
        }
        if (!privacyRequest.getRequestState().equals("PENDING")) {
            throw new DuplicateActionException("This privacy request has already been managed");
        }
        ContentCreator contentCreator = contentCreatorService.getContentCreatorByUsername(sender.getUsername());
        Company company = companyService.getCompanyByUsername(privacyRequest.getCompanyUsername());
        privacyRequest.setRequestState("ACCEPTED");
        privacyRequest.setContentCreator(contentCreator);
        privacyRequest.setCompany(company);
        privacyRequestRepository.save(privacyRequest);
    }

    public void declinePrivacyRequest(User sender, String requestId) throws Exception {
        if (!sender.getType().equals("CONTENT_CREATOR")) {
            throw new NotAuthorizedExcepcion("You must be a content creator to make this action.");
        }
        PrivacyRequest privacyRequest = getById(requestId).orElse(null);
        if (privacyRequest == null) {
            throw new NotFoundException("Privacy request not found");
        }
        if (!sender.getUsername().equals(privacyRequest.getContentCreatorUsername())) {
            throw new NotAuthorizedExcepcion("This privacy request does not belong to you");
        }
        if (!privacyRequest.getRequestState().equals("PENDING")) {
            throw new DuplicateActionException("This privacy request has already been managed");
        }
        ContentCreator contentCreator = contentCreatorService.getContentCreatorByUsername(sender.getUsername());
        Company company = companyService.getCompanyByUsername(privacyRequest.getCompanyUsername());
        privacyRequest.setRequestState("DECLINED");
        privacyRequest.setContentCreator(contentCreator);
        privacyRequest.setCompany(company);
        privacyRequestRepository.save(privacyRequest);
    }
    
}
