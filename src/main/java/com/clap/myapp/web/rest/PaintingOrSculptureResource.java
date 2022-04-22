package com.clap.myapp.web.rest;

import com.clap.myapp.domain.PaintingOrSculpture;
import com.clap.myapp.repository.PaintingOrSculptureRepository;
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
 * REST controller for managing {@link com.clap.myapp.domain.PaintingOrSculpture}.
 */
@RestController
@RequestMapping("/api")
public class PaintingOrSculptureResource {

    private final Logger log = LoggerFactory.getLogger(PaintingOrSculptureResource.class);

    private static final String ENTITY_NAME = "paintingOrSculpture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaintingOrSculptureRepository paintingOrSculptureRepository;

    public PaintingOrSculptureResource(PaintingOrSculptureRepository paintingOrSculptureRepository) {
        this.paintingOrSculptureRepository = paintingOrSculptureRepository;
    }

    /**
     * {@code POST  /painting-or-sculptures} : Create a new paintingOrSculpture.
     *
     * @param paintingOrSculpture the paintingOrSculpture to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paintingOrSculpture, or with status {@code 400 (Bad Request)} if the paintingOrSculpture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/painting-or-sculptures")
    public ResponseEntity<PaintingOrSculpture> createPaintingOrSculpture(@RequestBody PaintingOrSculpture paintingOrSculpture)
        throws URISyntaxException {
        log.debug("REST request to save PaintingOrSculpture : {}", paintingOrSculpture);
        if (paintingOrSculpture.getId() != null) {
            throw new BadRequestAlertException("A new paintingOrSculpture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaintingOrSculpture result = paintingOrSculptureRepository.save(paintingOrSculpture);
        return ResponseEntity
            .created(new URI("/api/painting-or-sculptures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /painting-or-sculptures/:id} : Updates an existing paintingOrSculpture.
     *
     * @param id the id of the paintingOrSculpture to save.
     * @param paintingOrSculpture the paintingOrSculpture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paintingOrSculpture,
     * or with status {@code 400 (Bad Request)} if the paintingOrSculpture is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paintingOrSculpture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/painting-or-sculptures/{id}")
    public ResponseEntity<PaintingOrSculpture> updatePaintingOrSculpture(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody PaintingOrSculpture paintingOrSculpture
    ) throws URISyntaxException {
        log.debug("REST request to update PaintingOrSculpture : {}, {}", id, paintingOrSculpture);
        if (paintingOrSculpture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paintingOrSculpture.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paintingOrSculptureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaintingOrSculpture result = paintingOrSculptureRepository.save(paintingOrSculpture);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paintingOrSculpture.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /painting-or-sculptures/:id} : Partial updates given fields of an existing paintingOrSculpture, field will ignore if it is null
     *
     * @param id the id of the paintingOrSculpture to save.
     * @param paintingOrSculpture the paintingOrSculpture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paintingOrSculpture,
     * or with status {@code 400 (Bad Request)} if the paintingOrSculpture is not valid,
     * or with status {@code 404 (Not Found)} if the paintingOrSculpture is not found,
     * or with status {@code 500 (Internal Server Error)} if the paintingOrSculpture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/painting-or-sculptures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaintingOrSculpture> partialUpdatePaintingOrSculpture(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody PaintingOrSculpture paintingOrSculpture
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaintingOrSculpture partially : {}, {}", id, paintingOrSculpture);
        if (paintingOrSculpture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paintingOrSculpture.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paintingOrSculptureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaintingOrSculpture> result = paintingOrSculptureRepository
            .findById(paintingOrSculpture.getId())
            .map(existingPaintingOrSculpture -> {
                if (paintingOrSculpture.getMaterials() != null) {
                    existingPaintingOrSculpture.setMaterials(paintingOrSculpture.getMaterials());
                }
                if (paintingOrSculpture.getTechniques() != null) {
                    existingPaintingOrSculpture.setTechniques(paintingOrSculpture.getTechniques());
                }
                if (paintingOrSculpture.getSize() != null) {
                    existingPaintingOrSculpture.setSize(paintingOrSculpture.getSize());
                }
                if (paintingOrSculpture.getPlace() != null) {
                    existingPaintingOrSculpture.setPlace(paintingOrSculpture.getPlace());
                }

                return existingPaintingOrSculpture;
            })
            .map(paintingOrSculptureRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paintingOrSculpture.getId())
        );
    }

    /**
     * {@code GET  /painting-or-sculptures} : get all the paintingOrSculptures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paintingOrSculptures in body.
     */
    @GetMapping("/painting-or-sculptures")
    public List<PaintingOrSculpture> getAllPaintingOrSculptures() {
        log.debug("REST request to get all PaintingOrSculptures");
        return paintingOrSculptureRepository.findAll();
    }

    /**
     * {@code GET  /painting-or-sculptures/:id} : get the "id" paintingOrSculpture.
     *
     * @param id the id of the paintingOrSculpture to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paintingOrSculpture, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/painting-or-sculptures/{id}")
    public ResponseEntity<PaintingOrSculpture> getPaintingOrSculpture(@PathVariable String id) {
        log.debug("REST request to get PaintingOrSculpture : {}", id);
        Optional<PaintingOrSculpture> paintingOrSculpture = paintingOrSculptureRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(paintingOrSculpture);
    }

    /**
     * {@code DELETE  /painting-or-sculptures/:id} : delete the "id" paintingOrSculpture.
     *
     * @param id the id of the paintingOrSculpture to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/painting-or-sculptures/{id}")
    public ResponseEntity<Void> deletePaintingOrSculpture(@PathVariable String id) {
        log.debug("REST request to delete PaintingOrSculpture : {}", id);
        paintingOrSculptureRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
