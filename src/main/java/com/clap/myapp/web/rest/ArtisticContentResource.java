package com.clap.myapp.web.rest;

import com.clap.myapp.domain.ArtisticContent;
import com.clap.myapp.repository.ArtisticContentRepository;
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
 * REST controller for managing {@link com.clap.myapp.domain.ArtisticContent}.
 */
@RestController
@RequestMapping("/api")
public class ArtisticContentResource {

    private final Logger log = LoggerFactory.getLogger(ArtisticContentResource.class);

    private static final String ENTITY_NAME = "artisticContent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArtisticContentRepository artisticContentRepository;

    public ArtisticContentResource(ArtisticContentRepository artisticContentRepository) {
        this.artisticContentRepository = artisticContentRepository;
    }

    /**
     * {@code POST  /artistic-contents} : Create a new artisticContent.
     *
     * @param artisticContent the artisticContent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new artisticContent, or with status {@code 400 (Bad Request)} if the artisticContent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/artistic-contents")
    public ResponseEntity<ArtisticContent> createArtisticContent(@RequestBody ArtisticContent artisticContent) throws URISyntaxException {
        log.debug("REST request to save ArtisticContent : {}", artisticContent);
        if (artisticContent.getId() != null) {
            throw new BadRequestAlertException("A new artisticContent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArtisticContent result = artisticContentRepository.save(artisticContent);
        return ResponseEntity
            .created(new URI("/api/artistic-contents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /artistic-contents/:id} : Updates an existing artisticContent.
     *
     * @param id the id of the artisticContent to save.
     * @param artisticContent the artisticContent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated artisticContent,
     * or with status {@code 400 (Bad Request)} if the artisticContent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the artisticContent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/artistic-contents/{id}")
    public ResponseEntity<ArtisticContent> updateArtisticContent(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ArtisticContent artisticContent
    ) throws URISyntaxException {
        log.debug("REST request to update ArtisticContent : {}, {}", id, artisticContent);
        if (artisticContent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, artisticContent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!artisticContentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ArtisticContent result = artisticContentRepository.save(artisticContent);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, artisticContent.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /artistic-contents/:id} : Partial updates given fields of an existing artisticContent, field will ignore if it is null
     *
     * @param id the id of the artisticContent to save.
     * @param artisticContent the artisticContent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated artisticContent,
     * or with status {@code 400 (Bad Request)} if the artisticContent is not valid,
     * or with status {@code 404 (Not Found)} if the artisticContent is not found,
     * or with status {@code 500 (Internal Server Error)} if the artisticContent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/artistic-contents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArtisticContent> partialUpdateArtisticContent(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ArtisticContent artisticContent
    ) throws URISyntaxException {
        log.debug("REST request to partial update ArtisticContent partially : {}, {}", id, artisticContent);
        if (artisticContent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, artisticContent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!artisticContentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArtisticContent> result = artisticContentRepository
            .findById(artisticContent.getId())
            .map(existingArtisticContent -> {
                if (artisticContent.getTitle() != null) {
                    existingArtisticContent.setTitle(artisticContent.getTitle());
                }
                if (artisticContent.getDescription() != null) {
                    existingArtisticContent.setDescription(artisticContent.getDescription());
                }
                if (artisticContent.getContentUrl() != null) {
                    existingArtisticContent.setContentUrl(artisticContent.getContentUrl());
                }
                if (artisticContent.getUploadDate() != null) {
                    existingArtisticContent.setUploadDate(artisticContent.getUploadDate());
                }
                if (artisticContent.getViewCount() != null) {
                    existingArtisticContent.setViewCount(artisticContent.getViewCount());
                }

                return existingArtisticContent;
            })
            .map(artisticContentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, artisticContent.getId())
        );
    }

    /**
     * {@code GET  /artistic-contents} : get all the artisticContents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of artisticContents in body.
     */
    @GetMapping("/artistic-contents")
    public List<ArtisticContent> getAllArtisticContents() {
        log.debug("REST request to get all ArtisticContents");
        return artisticContentRepository.findAll();
    }

    /**
     * {@code GET  /artistic-contents/:id} : get the "id" artisticContent.
     *
     * @param id the id of the artisticContent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the artisticContent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/artistic-contents/{id}")
    public ResponseEntity<ArtisticContent> getArtisticContent(@PathVariable String id) {
        log.debug("REST request to get ArtisticContent : {}", id);
        Optional<ArtisticContent> artisticContent = artisticContentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(artisticContent);
    }

    /**
     * {@code DELETE  /artistic-contents/:id} : delete the "id" artisticContent.
     *
     * @param id the id of the artisticContent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/artistic-contents/{id}")
    public ResponseEntity<Void> deleteArtisticContent(@PathVariable String id) {
        log.debug("REST request to delete ArtisticContent : {}", id);
        artisticContentRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
