package com.clap.myapp.web.rest;

import com.clap.myapp.domain.ContentCreator;
import com.clap.myapp.repository.ContentCreatorRepository;
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
 * REST controller for managing {@link com.clap.myapp.domain.ContentCreator}.
 */
@RestController
@RequestMapping("/api")
public class ContentCreatorResource {

    private final Logger log = LoggerFactory.getLogger(ContentCreatorResource.class);

    private static final String ENTITY_NAME = "contentCreator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContentCreatorRepository contentCreatorRepository;

    public ContentCreatorResource(ContentCreatorRepository contentCreatorRepository) {
        this.contentCreatorRepository = contentCreatorRepository;
    }

    /**
     * {@code POST  /content-creators} : Create a new contentCreator.
     *
     * @param contentCreator the contentCreator to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contentCreator, or with status {@code 400 (Bad Request)} if the contentCreator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/content-creators")
    public ResponseEntity<ContentCreator> createContentCreator(@RequestBody ContentCreator contentCreator) throws URISyntaxException {
        log.debug("REST request to save ContentCreator : {}", contentCreator);
        if (contentCreator.getId() != null) {
            throw new BadRequestAlertException("A new contentCreator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContentCreator result = contentCreatorRepository.save(contentCreator);
        return ResponseEntity
            .created(new URI("/api/content-creators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /content-creators/:id} : Updates an existing contentCreator.
     *
     * @param id the id of the contentCreator to save.
     * @param contentCreator the contentCreator to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentCreator,
     * or with status {@code 400 (Bad Request)} if the contentCreator is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contentCreator couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/content-creators/{id}")
    public ResponseEntity<ContentCreator> updateContentCreator(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ContentCreator contentCreator
    ) throws URISyntaxException {
        log.debug("REST request to update ContentCreator : {}, {}", id, contentCreator);
        if (contentCreator.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentCreator.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentCreatorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContentCreator result = contentCreatorRepository.save(contentCreator);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contentCreator.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /content-creators/:id} : Partial updates given fields of an existing contentCreator, field will ignore if it is null
     *
     * @param id the id of the contentCreator to save.
     * @param contentCreator the contentCreator to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentCreator,
     * or with status {@code 400 (Bad Request)} if the contentCreator is not valid,
     * or with status {@code 404 (Not Found)} if the contentCreator is not found,
     * or with status {@code 500 (Internal Server Error)} if the contentCreator couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/content-creators/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContentCreator> partialUpdateContentCreator(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ContentCreator contentCreator
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContentCreator partially : {}, {}", id, contentCreator);
        if (contentCreator.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentCreator.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentCreatorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContentCreator> result = contentCreatorRepository
            .findById(contentCreator.getId())
            .map(existingContentCreator -> {
                if (contentCreator.getFullName() != null) {
                    existingContentCreator.setFullName(contentCreator.getFullName());
                }
                if (contentCreator.getCountry() != null) {
                    existingContentCreator.setCountry(contentCreator.getCountry());
                }
                if (contentCreator.getCity() != null) {
                    existingContentCreator.setCity(contentCreator.getCity());
                }

                return existingContentCreator;
            })
            .map(contentCreatorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contentCreator.getId())
        );
    }

    /**
     * {@code GET  /content-creators} : get all the contentCreators.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contentCreators in body.
     */
    @GetMapping("/content-creators")
    public List<ContentCreator> getAllContentCreators() {
        log.debug("REST request to get all ContentCreators");
        return contentCreatorRepository.findAll();
    }

    /**
     * {@code GET  /content-creators/:id} : get the "id" contentCreator.
     *
     * @param id the id of the contentCreator to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contentCreator, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/content-creators/{id}")
    public ResponseEntity<ContentCreator> getContentCreator(@PathVariable String id) {
        log.debug("REST request to get ContentCreator : {}", id);
        Optional<ContentCreator> contentCreator = contentCreatorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contentCreator);
    }

    /**
     * {@code DELETE  /content-creators/:id} : delete the "id" contentCreator.
     *
     * @param id the id of the contentCreator to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/content-creators/{id}")
    public ResponseEntity<Void> deleteContentCreator(@PathVariable String id) {
        log.debug("REST request to delete ContentCreator : {}", id);
        contentCreatorRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
