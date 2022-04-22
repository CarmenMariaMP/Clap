package com.clap.myapp.web.rest;

import com.clap.myapp.domain.General;
import com.clap.myapp.repository.GeneralRepository;
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
 * REST controller for managing {@link com.clap.myapp.domain.General}.
 */
@RestController
@RequestMapping("/api")
public class GeneralResource {

    private final Logger log = LoggerFactory.getLogger(GeneralResource.class);

    private static final String ENTITY_NAME = "general";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GeneralRepository generalRepository;

    public GeneralResource(GeneralRepository generalRepository) {
        this.generalRepository = generalRepository;
    }

    /**
     * {@code POST  /generals} : Create a new general.
     *
     * @param general the general to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new general, or with status {@code 400 (Bad Request)} if the general has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/generals")
    public ResponseEntity<General> createGeneral(@RequestBody General general) throws URISyntaxException {
        log.debug("REST request to save General : {}", general);
        if (general.getId() != null) {
            throw new BadRequestAlertException("A new general cannot already have an ID", ENTITY_NAME, "idexists");
        }
        General result = generalRepository.save(general);
        return ResponseEntity
            .created(new URI("/api/generals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /generals/:id} : Updates an existing general.
     *
     * @param id the id of the general to save.
     * @param general the general to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated general,
     * or with status {@code 400 (Bad Request)} if the general is not valid,
     * or with status {@code 500 (Internal Server Error)} if the general couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/generals/{id}")
    public ResponseEntity<General> updateGeneral(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody General general
    ) throws URISyntaxException {
        log.debug("REST request to update General : {}, {}", id, general);
        if (general.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, general.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!generalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // no save call needed as we have no fields that can be updated
        General result = general;
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, general.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /generals/:id} : Partial updates given fields of an existing general, field will ignore if it is null
     *
     * @param id the id of the general to save.
     * @param general the general to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated general,
     * or with status {@code 400 (Bad Request)} if the general is not valid,
     * or with status {@code 404 (Not Found)} if the general is not found,
     * or with status {@code 500 (Internal Server Error)} if the general couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/generals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<General> partialUpdateGeneral(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody General general
    ) throws URISyntaxException {
        log.debug("REST request to partial update General partially : {}, {}", id, general);
        if (general.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, general.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!generalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<General> result = generalRepository
            .findById(general.getId())
            .map(existingGeneral -> {
                return existingGeneral;
            })// .map(generalRepository::save)
        ;

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, general.getId()));
    }

    /**
     * {@code GET  /generals} : get all the generals.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of generals in body.
     */
    @GetMapping("/generals")
    public List<General> getAllGenerals() {
        log.debug("REST request to get all Generals");
        return generalRepository.findAll();
    }

    /**
     * {@code GET  /generals/:id} : get the "id" general.
     *
     * @param id the id of the general to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the general, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/generals/{id}")
    public ResponseEntity<General> getGeneral(@PathVariable String id) {
        log.debug("REST request to get General : {}", id);
        Optional<General> general = generalRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(general);
    }

    /**
     * {@code DELETE  /generals/:id} : delete the "id" general.
     *
     * @param id the id of the general to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/generals/{id}")
    public ResponseEntity<Void> deleteGeneral(@PathVariable String id) {
        log.debug("REST request to delete General : {}", id);
        generalRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
