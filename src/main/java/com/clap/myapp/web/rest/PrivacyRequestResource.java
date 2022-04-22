package com.clap.myapp.web.rest;

import com.clap.myapp.domain.PrivacyRequest;
import com.clap.myapp.repository.PrivacyRequestRepository;
import com.clap.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.clap.myapp.domain.PrivacyRequest}.
 */
@RestController
@RequestMapping("/api")
public class PrivacyRequestResource {

    private final Logger log = LoggerFactory.getLogger(PrivacyRequestResource.class);

    private static final String ENTITY_NAME = "privacyRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrivacyRequestRepository privacyRequestRepository;

    public PrivacyRequestResource(PrivacyRequestRepository privacyRequestRepository) {
        this.privacyRequestRepository = privacyRequestRepository;
    }

    /**
     * {@code POST  /privacy-requests} : Create a new privacyRequest.
     *
     * @param privacyRequest the privacyRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new privacyRequest, or with status {@code 400 (Bad Request)} if the privacyRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/privacy-requests")
    public ResponseEntity<PrivacyRequest> createPrivacyRequest(@RequestBody PrivacyRequest privacyRequest) throws URISyntaxException {
        log.debug("REST request to save PrivacyRequest : {}", privacyRequest);
        if (privacyRequest.getId() != null) {
            throw new BadRequestAlertException("A new privacyRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrivacyRequest result = privacyRequestRepository.save(privacyRequest);
        return ResponseEntity
            .created(new URI("/api/privacy-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /privacy-requests/:id} : Updates an existing privacyRequest.
     *
     * @param id the id of the privacyRequest to save.
     * @param privacyRequest the privacyRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated privacyRequest,
     * or with status {@code 400 (Bad Request)} if the privacyRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the privacyRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/privacy-requests/{id}")
    public ResponseEntity<PrivacyRequest> updatePrivacyRequest(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody PrivacyRequest privacyRequest
    ) throws URISyntaxException {
        log.debug("REST request to update PrivacyRequest : {}, {}", id, privacyRequest);
        if (privacyRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, privacyRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!privacyRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PrivacyRequest result = privacyRequestRepository.save(privacyRequest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, privacyRequest.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /privacy-requests/:id} : Partial updates given fields of an existing privacyRequest, field will ignore if it is null
     *
     * @param id the id of the privacyRequest to save.
     * @param privacyRequest the privacyRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated privacyRequest,
     * or with status {@code 400 (Bad Request)} if the privacyRequest is not valid,
     * or with status {@code 404 (Not Found)} if the privacyRequest is not found,
     * or with status {@code 500 (Internal Server Error)} if the privacyRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/privacy-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PrivacyRequest> partialUpdatePrivacyRequest(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody PrivacyRequest privacyRequest
    ) throws URISyntaxException {
        log.debug("REST request to partial update PrivacyRequest partially : {}, {}", id, privacyRequest);
        if (privacyRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, privacyRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!privacyRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrivacyRequest> result = privacyRequestRepository
            .findById(privacyRequest.getId())
            .map(existingPrivacyRequest -> {
                if (privacyRequest.getRequestState() != null) {
                    existingPrivacyRequest.setRequestState(privacyRequest.getRequestState());
                }
                if (privacyRequest.getRequestDate() != null) {
                    existingPrivacyRequest.setRequestDate(privacyRequest.getRequestDate());
                }

                return existingPrivacyRequest;
            })
            .map(privacyRequestRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, privacyRequest.getId())
        );
    }

    /**
     * {@code GET  /privacy-requests} : get all the privacyRequests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of privacyRequests in body.
     */
    @GetMapping("/privacy-requests")
    public List<PrivacyRequest> getAllPrivacyRequests() {
        log.debug("REST request to get all PrivacyRequests");
        return privacyRequestRepository.findAll();
    }

    /**
     * {@code GET  /privacy-requests/:id} : get the "id" privacyRequest.
     *
     * @param id the id of the privacyRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the privacyRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/privacy-requests/{id}")
    public ResponseEntity<PrivacyRequest> getPrivacyRequest(@PathVariable String id) {
        log.debug("REST request to get PrivacyRequest : {}", id);
        Optional<PrivacyRequest> privacyRequest = privacyRequestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(privacyRequest);
    }

    /**
     * {@code DELETE  /privacy-requests/:id} : delete the "id" privacyRequest.
     *
     * @param id the id of the privacyRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/privacy-requests/{id}")
    public ResponseEntity<Void> deletePrivacyRequest(@PathVariable String id) {
        log.debug("REST request to delete PrivacyRequest : {}", id);
        privacyRequestRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
