package com.clap.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.clap.myapp.IntegrationTest;
import com.clap.myapp.domain.Cinema;
import com.clap.myapp.domain.enumeration.CinemaGenreType;
import com.clap.myapp.repository.CinemaRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link CinemaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CinemaResourceIT {

    private static final CinemaGenreType DEFAULT_GENRES = CinemaGenreType.ACTION;
    private static final CinemaGenreType UPDATED_GENRES = CinemaGenreType.ADVENTURE;

    private static final String ENTITY_API_URL = "/api/cinemas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private MockMvc restCinemaMockMvc;

    private Cinema cinema;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cinema createEntity() {
        Cinema cinema = new Cinema().genres(DEFAULT_GENRES);
        return cinema;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cinema createUpdatedEntity() {
        Cinema cinema = new Cinema().genres(UPDATED_GENRES);
        return cinema;
    }

    @BeforeEach
    public void initTest() {
        cinemaRepository.deleteAll();
        cinema = createEntity();
    }

    @Test
    void createCinema() throws Exception {
        int databaseSizeBeforeCreate = cinemaRepository.findAll().size();
        // Create the Cinema
        restCinemaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cinema))
            )
            .andExpect(status().isCreated());

        // Validate the Cinema in the database
        List<Cinema> cinemaList = cinemaRepository.findAll();
        assertThat(cinemaList).hasSize(databaseSizeBeforeCreate + 1);
        Cinema testCinema = cinemaList.get(cinemaList.size() - 1);
        assertThat(testCinema.getGenres()).isEqualTo(DEFAULT_GENRES);
    }

    @Test
    void createCinemaWithExistingId() throws Exception {
        // Create the Cinema with an existing ID
        cinema.setId("existing_id");

        int databaseSizeBeforeCreate = cinemaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCinemaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cinema))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cinema in the database
        List<Cinema> cinemaList = cinemaRepository.findAll();
        assertThat(cinemaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCinemas() throws Exception {
        // Initialize the database
        cinemaRepository.save(cinema);

        // Get all the cinemaList
        restCinemaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].genres").value(hasItem(DEFAULT_GENRES.toString())));
    }

    @Test
    void getCinema() throws Exception {
        // Initialize the database
        cinemaRepository.save(cinema);

        // Get the cinema
        restCinemaMockMvc
            .perform(get(ENTITY_API_URL_ID, cinema.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.genres").value(DEFAULT_GENRES.toString()));
    }

    @Test
    void getNonExistingCinema() throws Exception {
        // Get the cinema
        restCinemaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewCinema() throws Exception {
        // Initialize the database
        cinemaRepository.save(cinema);

        int databaseSizeBeforeUpdate = cinemaRepository.findAll().size();

        // Update the cinema
        Cinema updatedCinema = cinemaRepository.findById(cinema.getId()).get();
        updatedCinema.genres(UPDATED_GENRES);

        restCinemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCinema.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCinema))
            )
            .andExpect(status().isOk());

        // Validate the Cinema in the database
        List<Cinema> cinemaList = cinemaRepository.findAll();
        assertThat(cinemaList).hasSize(databaseSizeBeforeUpdate);
        Cinema testCinema = cinemaList.get(cinemaList.size() - 1);
        assertThat(testCinema.getGenres()).isEqualTo(UPDATED_GENRES);
    }

    @Test
    void putNonExistingCinema() throws Exception {
        int databaseSizeBeforeUpdate = cinemaRepository.findAll().size();
        cinema.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCinemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cinema.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cinema))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cinema in the database
        List<Cinema> cinemaList = cinemaRepository.findAll();
        assertThat(cinemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCinema() throws Exception {
        int databaseSizeBeforeUpdate = cinemaRepository.findAll().size();
        cinema.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCinemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cinema))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cinema in the database
        List<Cinema> cinemaList = cinemaRepository.findAll();
        assertThat(cinemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCinema() throws Exception {
        int databaseSizeBeforeUpdate = cinemaRepository.findAll().size();
        cinema.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCinemaMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cinema))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cinema in the database
        List<Cinema> cinemaList = cinemaRepository.findAll();
        assertThat(cinemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCinemaWithPatch() throws Exception {
        // Initialize the database
        cinemaRepository.save(cinema);

        int databaseSizeBeforeUpdate = cinemaRepository.findAll().size();

        // Update the cinema using partial update
        Cinema partialUpdatedCinema = new Cinema();
        partialUpdatedCinema.setId(cinema.getId());

        partialUpdatedCinema.genres(UPDATED_GENRES);

        restCinemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCinema.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCinema))
            )
            .andExpect(status().isOk());

        // Validate the Cinema in the database
        List<Cinema> cinemaList = cinemaRepository.findAll();
        assertThat(cinemaList).hasSize(databaseSizeBeforeUpdate);
        Cinema testCinema = cinemaList.get(cinemaList.size() - 1);
        assertThat(testCinema.getGenres()).isEqualTo(UPDATED_GENRES);
    }

    @Test
    void fullUpdateCinemaWithPatch() throws Exception {
        // Initialize the database
        cinemaRepository.save(cinema);

        int databaseSizeBeforeUpdate = cinemaRepository.findAll().size();

        // Update the cinema using partial update
        Cinema partialUpdatedCinema = new Cinema();
        partialUpdatedCinema.setId(cinema.getId());

        partialUpdatedCinema.genres(UPDATED_GENRES);

        restCinemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCinema.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCinema))
            )
            .andExpect(status().isOk());

        // Validate the Cinema in the database
        List<Cinema> cinemaList = cinemaRepository.findAll();
        assertThat(cinemaList).hasSize(databaseSizeBeforeUpdate);
        Cinema testCinema = cinemaList.get(cinemaList.size() - 1);
        assertThat(testCinema.getGenres()).isEqualTo(UPDATED_GENRES);
    }

    @Test
    void patchNonExistingCinema() throws Exception {
        int databaseSizeBeforeUpdate = cinemaRepository.findAll().size();
        cinema.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCinemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cinema.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cinema))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cinema in the database
        List<Cinema> cinemaList = cinemaRepository.findAll();
        assertThat(cinemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCinema() throws Exception {
        int databaseSizeBeforeUpdate = cinemaRepository.findAll().size();
        cinema.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCinemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cinema))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cinema in the database
        List<Cinema> cinemaList = cinemaRepository.findAll();
        assertThat(cinemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCinema() throws Exception {
        int databaseSizeBeforeUpdate = cinemaRepository.findAll().size();
        cinema.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCinemaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cinema))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cinema in the database
        List<Cinema> cinemaList = cinemaRepository.findAll();
        assertThat(cinemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCinema() throws Exception {
        // Initialize the database
        cinemaRepository.save(cinema);

        int databaseSizeBeforeDelete = cinemaRepository.findAll().size();

        // Delete the cinema
        restCinemaMockMvc
            .perform(delete(ENTITY_API_URL_ID, cinema.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cinema> cinemaList = cinemaRepository.findAll();
        assertThat(cinemaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
