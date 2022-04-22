package com.clap.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.clap.myapp.IntegrationTest;
import com.clap.myapp.domain.Dance;
import com.clap.myapp.domain.enumeration.DanceGenreType;
import com.clap.myapp.repository.DanceRepository;
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
 * Integration tests for the {@link DanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DanceResourceIT {

    private static final String DEFAULT_MUSIC = "AAAAAAAAAA";
    private static final String UPDATED_MUSIC = "BBBBBBBBBB";

    private static final DanceGenreType DEFAULT_GENRES = DanceGenreType.FLAMENCO;
    private static final DanceGenreType UPDATED_GENRES = DanceGenreType.SPANISH_DANCE;

    private static final String ENTITY_API_URL = "/api/dances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DanceRepository danceRepository;

    @Autowired
    private MockMvc restDanceMockMvc;

    private Dance dance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dance createEntity() {
        Dance dance = new Dance().music(DEFAULT_MUSIC).genres(DEFAULT_GENRES);
        return dance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dance createUpdatedEntity() {
        Dance dance = new Dance().music(UPDATED_MUSIC).genres(UPDATED_GENRES);
        return dance;
    }

    @BeforeEach
    public void initTest() {
        danceRepository.deleteAll();
        dance = createEntity();
    }

    @Test
    void createDance() throws Exception {
        int databaseSizeBeforeCreate = danceRepository.findAll().size();
        // Create the Dance
        restDanceMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dance))
            )
            .andExpect(status().isCreated());

        // Validate the Dance in the database
        List<Dance> danceList = danceRepository.findAll();
        assertThat(danceList).hasSize(databaseSizeBeforeCreate + 1);
        Dance testDance = danceList.get(danceList.size() - 1);
        assertThat(testDance.getMusic()).isEqualTo(DEFAULT_MUSIC);
        assertThat(testDance.getGenres()).isEqualTo(DEFAULT_GENRES);
    }

    @Test
    void createDanceWithExistingId() throws Exception {
        // Create the Dance with an existing ID
        dance.setId("existing_id");

        int databaseSizeBeforeCreate = danceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDanceMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dance in the database
        List<Dance> danceList = danceRepository.findAll();
        assertThat(danceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDances() throws Exception {
        // Initialize the database
        danceRepository.save(dance);

        // Get all the danceList
        restDanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].music").value(hasItem(DEFAULT_MUSIC)))
            .andExpect(jsonPath("$.[*].genres").value(hasItem(DEFAULT_GENRES.toString())));
    }

    @Test
    void getDance() throws Exception {
        // Initialize the database
        danceRepository.save(dance);

        // Get the dance
        restDanceMockMvc
            .perform(get(ENTITY_API_URL_ID, dance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.music").value(DEFAULT_MUSIC))
            .andExpect(jsonPath("$.genres").value(DEFAULT_GENRES.toString()));
    }

    @Test
    void getNonExistingDance() throws Exception {
        // Get the dance
        restDanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewDance() throws Exception {
        // Initialize the database
        danceRepository.save(dance);

        int databaseSizeBeforeUpdate = danceRepository.findAll().size();

        // Update the dance
        Dance updatedDance = danceRepository.findById(dance.getId()).get();
        updatedDance.music(UPDATED_MUSIC).genres(UPDATED_GENRES);

        restDanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDance.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDance))
            )
            .andExpect(status().isOk());

        // Validate the Dance in the database
        List<Dance> danceList = danceRepository.findAll();
        assertThat(danceList).hasSize(databaseSizeBeforeUpdate);
        Dance testDance = danceList.get(danceList.size() - 1);
        assertThat(testDance.getMusic()).isEqualTo(UPDATED_MUSIC);
        assertThat(testDance.getGenres()).isEqualTo(UPDATED_GENRES);
    }

    @Test
    void putNonExistingDance() throws Exception {
        int databaseSizeBeforeUpdate = danceRepository.findAll().size();
        dance.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dance.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dance in the database
        List<Dance> danceList = danceRepository.findAll();
        assertThat(danceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDance() throws Exception {
        int databaseSizeBeforeUpdate = danceRepository.findAll().size();
        dance.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dance in the database
        List<Dance> danceList = danceRepository.findAll();
        assertThat(danceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDance() throws Exception {
        int databaseSizeBeforeUpdate = danceRepository.findAll().size();
        dance.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDanceMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dance in the database
        List<Dance> danceList = danceRepository.findAll();
        assertThat(danceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDanceWithPatch() throws Exception {
        // Initialize the database
        danceRepository.save(dance);

        int databaseSizeBeforeUpdate = danceRepository.findAll().size();

        // Update the dance using partial update
        Dance partialUpdatedDance = new Dance();
        partialUpdatedDance.setId(dance.getId());

        partialUpdatedDance.music(UPDATED_MUSIC).genres(UPDATED_GENRES);

        restDanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDance.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDance))
            )
            .andExpect(status().isOk());

        // Validate the Dance in the database
        List<Dance> danceList = danceRepository.findAll();
        assertThat(danceList).hasSize(databaseSizeBeforeUpdate);
        Dance testDance = danceList.get(danceList.size() - 1);
        assertThat(testDance.getMusic()).isEqualTo(UPDATED_MUSIC);
        assertThat(testDance.getGenres()).isEqualTo(UPDATED_GENRES);
    }

    @Test
    void fullUpdateDanceWithPatch() throws Exception {
        // Initialize the database
        danceRepository.save(dance);

        int databaseSizeBeforeUpdate = danceRepository.findAll().size();

        // Update the dance using partial update
        Dance partialUpdatedDance = new Dance();
        partialUpdatedDance.setId(dance.getId());

        partialUpdatedDance.music(UPDATED_MUSIC).genres(UPDATED_GENRES);

        restDanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDance.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDance))
            )
            .andExpect(status().isOk());

        // Validate the Dance in the database
        List<Dance> danceList = danceRepository.findAll();
        assertThat(danceList).hasSize(databaseSizeBeforeUpdate);
        Dance testDance = danceList.get(danceList.size() - 1);
        assertThat(testDance.getMusic()).isEqualTo(UPDATED_MUSIC);
        assertThat(testDance.getGenres()).isEqualTo(UPDATED_GENRES);
    }

    @Test
    void patchNonExistingDance() throws Exception {
        int databaseSizeBeforeUpdate = danceRepository.findAll().size();
        dance.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dance.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dance in the database
        List<Dance> danceList = danceRepository.findAll();
        assertThat(danceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDance() throws Exception {
        int databaseSizeBeforeUpdate = danceRepository.findAll().size();
        dance.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dance in the database
        List<Dance> danceList = danceRepository.findAll();
        assertThat(danceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDance() throws Exception {
        int databaseSizeBeforeUpdate = danceRepository.findAll().size();
        dance.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDanceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dance in the database
        List<Dance> danceList = danceRepository.findAll();
        assertThat(danceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDance() throws Exception {
        // Initialize the database
        danceRepository.save(dance);

        int databaseSizeBeforeDelete = danceRepository.findAll().size();

        // Delete the dance
        restDanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, dance.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dance> danceList = danceRepository.findAll();
        assertThat(danceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
