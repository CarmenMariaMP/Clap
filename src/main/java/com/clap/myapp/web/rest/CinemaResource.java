package com.clap.myapp.web.rest;

import com.clap.myapp.domain.Cinema;
import com.clap.myapp.repository.CinemaRepository;
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
 * REST controller for managing {@link com.clap.myapp.domain.Cinema}.
 */
@RestController
@RequestMapping("/api")
public class CinemaResource {

    private final Logger log = LoggerFactory.getLogger(CinemaResource.class);

    private static final String ENTITY_NAME = "cinema";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CinemaRepository cinemaRepository;

    public CinemaResource(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    /**
     * {@code POST  /cinemas} : Create a new cinema.
     *
     * @param cinema the cinema to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cinema, or with status {@code 400 (Bad Request)} if the cinema has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cinemas")
    public ResponseEntity<Cinema> createCinema(@RequestBody Cinema cinema) throws URISyntaxException {
        log.debug("REST request to save Cinema : {}", cinema);
        if (cinema.getId() != null) {
            throw new BadRequestAlertException("A new cinema cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cinema result = cinemaRepository.save(cinema);
        return ResponseEntity
            .created(new URI("/api/cinemas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /cinemas/:id} : Updates an existing cinema.
     *
     * @param id the id of the cinema to save.
     * @param cinema the cinema to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cinema,
     * or with status {@code 400 (Bad Request)} if the cinema is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cinema couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cinemas/{id}")
    public ResponseEntity<Cinema> updateCinema(@PathVariable(value = "id", required = false) final String id, @RequestBody Cinema cinema)
        throws URISyntaxException {
        log.debug("REST request to update Cinema : {}, {}", id, cinema);
        if (cinema.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cinema.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cinemaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cinema result = cinemaRepository.save(cinema);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cinema.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /cinemas/:id} : Partial updates given fields of an existing cinema, field will ignore if it is null
     *
     * @param id the id of the cinema to save.
     * @param cinema the cinema to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cinema,
     * or with status {@code 400 (Bad Request)} if the cinema is not valid,
     * or with status {@code 404 (Not Found)} if the cinema is not found,
     * or with status {@code 500 (Internal Server Error)} if the cinema couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cinemas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cinema> partialUpdateCinema(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Cinema cinema
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cinema partially : {}, {}", id, cinema);
        if (cinema.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cinema.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cinemaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cinema> result = cinemaRepository
            .findById(cinema.getId())
            .map(existingCinema -> {
                if (cinema.getGenres() != null) {
                    existingCinema.setGenres(cinema.getGenres());
                }

                return existingCinema;
            })
            .map(cinemaRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cinema.getId()));
    }

    /**
     * {@code GET  /cinemas} : get all the cinemas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cinemas in body.
     */
    @GetMapping("/cinemas")
    public List<Cinema> getAllCinemas() {
        log.debug("REST request to get all Cinemas");
        return cinemaRepository.findAll();
    }

    /**
     * {@code GET  /cinemas/:id} : get the "id" cinema.
     *
     * @param id the id of the cinema to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cinema, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cinemas/{id}")
    public ResponseEntity<Cinema> getCinema(@PathVariable String id) {
        log.debug("REST request to get Cinema : {}", id);
        Optional<Cinema> cinema = cinemaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cinema);
    }

    /**
     * {@code DELETE  /cinemas/:id} : delete the "id" cinema.
     *
     * @param id the id of the cinema to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cinemas/{id}")
    public ResponseEntity<Void> deleteCinema(@PathVariable String id) {
        log.debug("REST request to delete Cinema : {}", id);
        cinemaRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
