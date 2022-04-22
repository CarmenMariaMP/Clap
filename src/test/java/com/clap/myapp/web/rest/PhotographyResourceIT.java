package com.clap.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.clap.myapp.IntegrationTest;
import com.clap.myapp.domain.Photography;
import com.clap.myapp.repository.PhotographyRepository;
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
 * Integration tests for the {@link PhotographyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PhotographyResourceIT {

    private static final String DEFAULT_CAMERA = "AAAAAAAAAA";
    private static final String UPDATED_CAMERA = "BBBBBBBBBB";

    private static final String DEFAULT_TECHNIQUES = "AAAAAAAAAA";
    private static final String UPDATED_TECHNIQUES = "BBBBBBBBBB";

    private static final String DEFAULT_SIZE = "AAAAAAAAAA";
    private static final String UPDATED_SIZE = "BBBBBBBBBB";

    private static final String DEFAULT_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_PLACE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/photographies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PhotographyRepository photographyRepository;

    @Autowired
    private MockMvc restPhotographyMockMvc;

    private Photography photography;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Photography createEntity() {
        Photography photography = new Photography()
            .camera(DEFAULT_CAMERA)
            .techniques(DEFAULT_TECHNIQUES)
            .size(DEFAULT_SIZE)
            .place(DEFAULT_PLACE);
        return photography;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Photography createUpdatedEntity() {
        Photography photography = new Photography()
            .camera(UPDATED_CAMERA)
            .techniques(UPDATED_TECHNIQUES)
            .size(UPDATED_SIZE)
            .place(UPDATED_PLACE);
        return photography;
    }

    @BeforeEach
    public void initTest() {
        photographyRepository.deleteAll();
        photography = createEntity();
    }

    @Test
    void createPhotography() throws Exception {
        int databaseSizeBeforeCreate = photographyRepository.findAll().size();
        // Create the Photography
        restPhotographyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photography))
            )
            .andExpect(status().isCreated());

        // Validate the Photography in the database
        List<Photography> photographyList = photographyRepository.findAll();
        assertThat(photographyList).hasSize(databaseSizeBeforeCreate + 1);
        Photography testPhotography = photographyList.get(photographyList.size() - 1);
        assertThat(testPhotography.getCamera()).isEqualTo(DEFAULT_CAMERA);
        assertThat(testPhotography.getTechniques()).isEqualTo(DEFAULT_TECHNIQUES);
        assertThat(testPhotography.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testPhotography.getPlace()).isEqualTo(DEFAULT_PLACE);
    }

    @Test
    void createPhotographyWithExistingId() throws Exception {
        // Create the Photography with an existing ID
        photography.setId("existing_id");

        int databaseSizeBeforeCreate = photographyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhotographyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photography))
            )
            .andExpect(status().isBadRequest());

        // Validate the Photography in the database
        List<Photography> photographyList = photographyRepository.findAll();
        assertThat(photographyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPhotographies() throws Exception {
        // Initialize the database
        photographyRepository.save(photography);

        // Get all the photographyList
        restPhotographyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].camera").value(hasItem(DEFAULT_CAMERA)))
            .andExpect(jsonPath("$.[*].techniques").value(hasItem(DEFAULT_TECHNIQUES)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)));
    }

    @Test
    void getPhotography() throws Exception {
        // Initialize the database
        photographyRepository.save(photography);

        // Get the photography
        restPhotographyMockMvc
            .perform(get(ENTITY_API_URL_ID, photography.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.camera").value(DEFAULT_CAMERA))
            .andExpect(jsonPath("$.techniques").value(DEFAULT_TECHNIQUES))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE));
    }

    @Test
    void getNonExistingPhotography() throws Exception {
        // Get the photography
        restPhotographyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewPhotography() throws Exception {
        // Initialize the database
        photographyRepository.save(photography);

        int databaseSizeBeforeUpdate = photographyRepository.findAll().size();

        // Update the photography
        Photography updatedPhotography = photographyRepository.findById(photography.getId()).get();
        updatedPhotography.camera(UPDATED_CAMERA).techniques(UPDATED_TECHNIQUES).size(UPDATED_SIZE).place(UPDATED_PLACE);

        restPhotographyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPhotography.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPhotography))
            )
            .andExpect(status().isOk());

        // Validate the Photography in the database
        List<Photography> photographyList = photographyRepository.findAll();
        assertThat(photographyList).hasSize(databaseSizeBeforeUpdate);
        Photography testPhotography = photographyList.get(photographyList.size() - 1);
        assertThat(testPhotography.getCamera()).isEqualTo(UPDATED_CAMERA);
        assertThat(testPhotography.getTechniques()).isEqualTo(UPDATED_TECHNIQUES);
        assertThat(testPhotography.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testPhotography.getPlace()).isEqualTo(UPDATED_PLACE);
    }

    @Test
    void putNonExistingPhotography() throws Exception {
        int databaseSizeBeforeUpdate = photographyRepository.findAll().size();
        photography.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhotographyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, photography.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photography))
            )
            .andExpect(status().isBadRequest());

        // Validate the Photography in the database
        List<Photography> photographyList = photographyRepository.findAll();
        assertThat(photographyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPhotography() throws Exception {
        int databaseSizeBeforeUpdate = photographyRepository.findAll().size();
        photography.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotographyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photography))
            )
            .andExpect(status().isBadRequest());

        // Validate the Photography in the database
        List<Photography> photographyList = photographyRepository.findAll();
        assertThat(photographyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPhotography() throws Exception {
        int databaseSizeBeforeUpdate = photographyRepository.findAll().size();
        photography.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotographyMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photography))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Photography in the database
        List<Photography> photographyList = photographyRepository.findAll();
        assertThat(photographyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePhotographyWithPatch() throws Exception {
        // Initialize the database
        photographyRepository.save(photography);

        int databaseSizeBeforeUpdate = photographyRepository.findAll().size();

        // Update the photography using partial update
        Photography partialUpdatedPhotography = new Photography();
        partialUpdatedPhotography.setId(photography.getId());

        partialUpdatedPhotography.camera(UPDATED_CAMERA).techniques(UPDATED_TECHNIQUES);

        restPhotographyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhotography.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhotography))
            )
            .andExpect(status().isOk());

        // Validate the Photography in the database
        List<Photography> photographyList = photographyRepository.findAll();
        assertThat(photographyList).hasSize(databaseSizeBeforeUpdate);
        Photography testPhotography = photographyList.get(photographyList.size() - 1);
        assertThat(testPhotography.getCamera()).isEqualTo(UPDATED_CAMERA);
        assertThat(testPhotography.getTechniques()).isEqualTo(UPDATED_TECHNIQUES);
        assertThat(testPhotography.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testPhotography.getPlace()).isEqualTo(DEFAULT_PLACE);
    }

    @Test
    void fullUpdatePhotographyWithPatch() throws Exception {
        // Initialize the database
        photographyRepository.save(photography);

        int databaseSizeBeforeUpdate = photographyRepository.findAll().size();

        // Update the photography using partial update
        Photography partialUpdatedPhotography = new Photography();
        partialUpdatedPhotography.setId(photography.getId());

        partialUpdatedPhotography.camera(UPDATED_CAMERA).techniques(UPDATED_TECHNIQUES).size(UPDATED_SIZE).place(UPDATED_PLACE);

        restPhotographyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhotography.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhotography))
            )
            .andExpect(status().isOk());

        // Validate the Photography in the database
        List<Photography> photographyList = photographyRepository.findAll();
        assertThat(photographyList).hasSize(databaseSizeBeforeUpdate);
        Photography testPhotography = photographyList.get(photographyList.size() - 1);
        assertThat(testPhotography.getCamera()).isEqualTo(UPDATED_CAMERA);
        assertThat(testPhotography.getTechniques()).isEqualTo(UPDATED_TECHNIQUES);
        assertThat(testPhotography.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testPhotography.getPlace()).isEqualTo(UPDATED_PLACE);
    }

    @Test
    void patchNonExistingPhotography() throws Exception {
        int databaseSizeBeforeUpdate = photographyRepository.findAll().size();
        photography.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhotographyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, photography.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(photography))
            )
            .andExpect(status().isBadRequest());

        // Validate the Photography in the database
        List<Photography> photographyList = photographyRepository.findAll();
        assertThat(photographyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPhotography() throws Exception {
        int databaseSizeBeforeUpdate = photographyRepository.findAll().size();
        photography.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotographyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(photography))
            )
            .andExpect(status().isBadRequest());

        // Validate the Photography in the database
        List<Photography> photographyList = photographyRepository.findAll();
        assertThat(photographyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPhotography() throws Exception {
        int databaseSizeBeforeUpdate = photographyRepository.findAll().size();
        photography.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotographyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(photography))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Photography in the database
        List<Photography> photographyList = photographyRepository.findAll();
        assertThat(photographyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePhotography() throws Exception {
        // Initialize the database
        photographyRepository.save(photography);

        int databaseSizeBeforeDelete = photographyRepository.findAll().size();

        // Delete the photography
        restPhotographyMockMvc
            .perform(delete(ENTITY_API_URL_ID, photography.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Photography> photographyList = photographyRepository.findAll();
        assertThat(photographyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
