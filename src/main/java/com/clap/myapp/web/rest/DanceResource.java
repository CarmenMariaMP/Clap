package com.clap.myapp.web.rest;

import com.clap.myapp.domain.Dance;
import com.clap.myapp.repository.DanceRepository;
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
 * REST controller for managing {@link com.clap.myapp.domain.Dance}.
 */
@RestController
@RequestMapping("/api")
public class DanceResource {

    private final Logger log = LoggerFactory.getLogger(DanceResource.class);

    private static final String ENTITY_NAME = "dance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DanceRepository danceRepository;

    public DanceResource(DanceRepository danceRepository) {
        this.danceRepository = danceRepository;
    }

    /**
     * {@code POST  /dances} : Create a new dance.
     *
     * @param dance the dance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dance, or with status {@code 400 (Bad Request)} if the dance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dances")
    public ResponseEntity<Dance> createDance(@RequestBody Dance dance) throws URISyntaxException {
        log.debug("REST request to save Dance : {}", dance);
        if (dance.getId() != null) {
            throw new BadRequestAlertException("A new dance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dance result = danceRepository.save(dance);
        return ResponseEntity
            .created(new URI("/api/dances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /dances/:id} : Updates an existing dance.
     *
     * @param id the id of the dance to save.
     * @param dance the dance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dance,
     * or with status {@code 400 (Bad Request)} if the dance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dances/{id}")
    public ResponseEntity<Dance> updateDance(@PathVariable(value = "id", required = false) final String id, @RequestBody Dance dance)
        throws URISyntaxException {
        log.debug("REST request to update Dance : {}, {}", id, dance);
        if (dance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!danceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dance result = danceRepository.save(dance);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dance.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /dances/:id} : Partial updates given fields of an existing dance, field will ignore if it is null
     *
     * @param id the id of the dance to save.
     * @param dance the dance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dance,
     * or with status {@code 400 (Bad Request)} if the dance is not valid,
     * or with status {@code 404 (Not Found)} if the dance is not found,
     * or with status {@code 500 (Internal Server Error)} if the dance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Dance> partialUpdateDance(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Dance dance
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dance partially : {}, {}", id, dance);
        if (dance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!danceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dance> result = danceRepository
            .findById(dance.getId())
            .map(existingDance -> {
                if (dance.getMusic() != null) {
                    existingDance.setMusic(dance.getMusic());
                }
                if (dance.getGenres() != null) {
                    existingDance.setGenres(dance.getGenres());
                }

                return existingDance;
            })
            .map(danceRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dance.getId()));
    }

    /**
     * {@code GET  /dances} : get all the dances.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dances in body.
     */
    @GetMapping("/dances")
    public List<Dance> getAllDances() {
        log.debug("REST request to get all Dances");
        return danceRepository.findAll();
    }

    /**
     * {@code GET  /dances/:id} : get the "id" dance.
     *
     * @param id the id of the dance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dances/{id}")
    public ResponseEntity<Dance> getDance(@PathVariable String id) {
        log.debug("REST request to get Dance : {}", id);
        Optional<Dance> dance = danceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dance);
    }

    /**
     * {@code DELETE  /dances/:id} : delete the "id" dance.
     *
     * @param id the id of the dance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dances/{id}")
    public ResponseEntity<Void> deleteDance(@PathVariable String id) {
        log.debug("REST request to delete Dance : {}", id);
        danceRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
