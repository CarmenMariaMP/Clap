package com.clap.myapp.web.rest;

import com.clap.myapp.domain.Photography;
import com.clap.myapp.repository.PhotographyRepository;
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
 * REST controller for managing {@link com.clap.myapp.domain.Photography}.
 */
@RestController
@RequestMapping("/api")
public class PhotographyResource {

    private final Logger log = LoggerFactory.getLogger(PhotographyResource.class);

    private static final String ENTITY_NAME = "photography";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhotographyRepository photographyRepository;

    public PhotographyResource(PhotographyRepository photographyRepository) {
        this.photographyRepository = photographyRepository;
    }

    /**
     * {@code POST  /photographies} : Create a new photography.
     *
     * @param photography the photography to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new photography, or with status {@code 400 (Bad Request)} if the photography has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/photographies")
    public ResponseEntity<Photography> createPhotography(@RequestBody Photography photography) throws URISyntaxException {
        log.debug("REST request to save Photography : {}", photography);
        if (photography.getId() != null) {
            throw new BadRequestAlertException("A new photography cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Photography result = photographyRepository.save(photography);
        return ResponseEntity
            .created(new URI("/api/photographies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /photographies/:id} : Updates an existing photography.
     *
     * @param id the id of the photography to save.
     * @param photography the photography to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated photography,
     * or with status {@code 400 (Bad Request)} if the photography is not valid,
     * or with status {@code 500 (Internal Server Error)} if the photography couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/photographies/{id}")
    public ResponseEntity<Photography> updatePhotography(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Photography photography
    ) throws URISyntaxException {
        log.debug("REST request to update Photography : {}, {}", id, photography);
        if (photography.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, photography.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!photographyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Photography result = photographyRepository.save(photography);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, photography.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /photographies/:id} : Partial updates given fields of an existing photography, field will ignore if it is null
     *
     * @param id the id of the photography to save.
     * @param photography the photography to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated photography,
     * or with status {@code 400 (Bad Request)} if the photography is not valid,
     * or with status {@code 404 (Not Found)} if the photography is not found,
     * or with status {@code 500 (Internal Server Error)} if the photography couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/photographies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Photography> partialUpdatePhotography(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Photography photography
    ) throws URISyntaxException {
        log.debug("REST request to partial update Photography partially : {}, {}", id, photography);
        if (photography.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, photography.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!photographyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Photography> result = photographyRepository
            .findById(photography.getId())
            .map(existingPhotography -> {
                if (photography.getCamera() != null) {
                    existingPhotography.setCamera(photography.getCamera());
                }
                if (photography.getTechniques() != null) {
                    existingPhotography.setTechniques(photography.getTechniques());
                }
                if (photography.getSize() != null) {
                    existingPhotography.setSize(photography.getSize());
                }
                if (photography.getPlace() != null) {
                    existingPhotography.setPlace(photography.getPlace());
                }

                return existingPhotography;
            })
            .map(photographyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, photography.getId())
        );
    }

    /**
     * {@code GET  /photographies} : get all the photographies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of photographies in body.
     */
    @GetMapping("/photographies")
    public List<Photography> getAllPhotographies() {
        log.debug("REST request to get all Photographies");
        return photographyRepository.findAll();
    }

    /**
     * {@code GET  /photographies/:id} : get the "id" photography.
     *
     * @param id the id of the photography to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the photography, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/photographies/{id}")
    public ResponseEntity<Photography> getPhotography(@PathVariable String id) {
        log.debug("REST request to get Photography : {}", id);
        Optional<Photography> photography = photographyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(photography);
    }

    /**
     * {@code DELETE  /photographies/:id} : delete the "id" photography.
     *
     * @param id the id of the photography to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/photographies/{id}")
    public ResponseEntity<Void> deletePhotography(@PathVariable String id) {
        log.debug("REST request to delete Photography : {}", id);
        photographyRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
