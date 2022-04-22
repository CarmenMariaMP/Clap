package com.clap.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.clap.myapp.IntegrationTest;
import com.clap.myapp.domain.PaintingOrSculpture;
import com.clap.myapp.repository.PaintingOrSculptureRepository;
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
 * Integration tests for the {@link PaintingOrSculptureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaintingOrSculptureResourceIT {

    private static final String DEFAULT_MATERIALS = "AAAAAAAAAA";
    private static final String UPDATED_MATERIALS = "BBBBBBBBBB";

    private static final String DEFAULT_TECHNIQUES = "AAAAAAAAAA";
    private static final String UPDATED_TECHNIQUES = "BBBBBBBBBB";

    private static final String DEFAULT_SIZE = "AAAAAAAAAA";
    private static final String UPDATED_SIZE = "BBBBBBBBBB";

    private static final String DEFAULT_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_PLACE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/painting-or-sculptures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PaintingOrSculptureRepository paintingOrSculptureRepository;

    @Autowired
    private MockMvc restPaintingOrSculptureMockMvc;

    private PaintingOrSculpture paintingOrSculpture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaintingOrSculpture createEntity() {
        PaintingOrSculpture paintingOrSculpture = new PaintingOrSculpture()
            .materials(DEFAULT_MATERIALS)
            .techniques(DEFAULT_TECHNIQUES)
            .size(DEFAULT_SIZE)
            .place(DEFAULT_PLACE);
        return paintingOrSculpture;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaintingOrSculpture createUpdatedEntity() {
        PaintingOrSculpture paintingOrSculpture = new PaintingOrSculpture()
            .materials(UPDATED_MATERIALS)
            .techniques(UPDATED_TECHNIQUES)
            .size(UPDATED_SIZE)
            .place(UPDATED_PLACE);
        return paintingOrSculpture;
    }

    @BeforeEach
    public void initTest() {
        paintingOrSculptureRepository.deleteAll();
        paintingOrSculpture = createEntity();
    }

    @Test
    void createPaintingOrSculpture() throws Exception {
        int databaseSizeBeforeCreate = paintingOrSculptureRepository.findAll().size();
        // Create the PaintingOrSculpture
        restPaintingOrSculptureMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paintingOrSculpture))
            )
            .andExpect(status().isCreated());

        // Validate the PaintingOrSculpture in the database
        List<PaintingOrSculpture> paintingOrSculptureList = paintingOrSculptureRepository.findAll();
        assertThat(paintingOrSculptureList).hasSize(databaseSizeBeforeCreate + 1);
        PaintingOrSculpture testPaintingOrSculpture = paintingOrSculptureList.get(paintingOrSculptureList.size() - 1);
        assertThat(testPaintingOrSculpture.getMaterials()).isEqualTo(DEFAULT_MATERIALS);
        assertThat(testPaintingOrSculpture.getTechniques()).isEqualTo(DEFAULT_TECHNIQUES);
        assertThat(testPaintingOrSculpture.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testPaintingOrSculpture.getPlace()).isEqualTo(DEFAULT_PLACE);
    }

    @Test
    void createPaintingOrSculptureWithExistingId() throws Exception {
        // Create the PaintingOrSculpture with an existing ID
        paintingOrSculpture.setId("existing_id");

        int databaseSizeBeforeCreate = paintingOrSculptureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaintingOrSculptureMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paintingOrSculpture))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaintingOrSculpture in the database
        List<PaintingOrSculpture> paintingOrSculptureList = paintingOrSculptureRepository.findAll();
        assertThat(paintingOrSculptureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPaintingOrSculptures() throws Exception {
        // Initialize the database
        paintingOrSculptureRepository.save(paintingOrSculpture);

        // Get all the paintingOrSculptureList
        restPaintingOrSculptureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].materials").value(hasItem(DEFAULT_MATERIALS)))
            .andExpect(jsonPath("$.[*].techniques").value(hasItem(DEFAULT_TECHNIQUES)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)))
            .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE)));
    }

    @Test
    void getPaintingOrSculpture() throws Exception {
        // Initialize the database
        paintingOrSculptureRepository.save(paintingOrSculpture);

        // Get the paintingOrSculpture
        restPaintingOrSculptureMockMvc
            .perform(get(ENTITY_API_URL_ID, paintingOrSculpture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.materials").value(DEFAULT_MATERIALS))
            .andExpect(jsonPath("$.techniques").value(DEFAULT_TECHNIQUES))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE));
    }

    @Test
    void getNonExistingPaintingOrSculpture() throws Exception {
        // Get the paintingOrSculpture
        restPaintingOrSculptureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewPaintingOrSculpture() throws Exception {
        // Initialize the database
        paintingOrSculptureRepository.save(paintingOrSculpture);

        int databaseSizeBeforeUpdate = paintingOrSculptureRepository.findAll().size();

        // Update the paintingOrSculpture
        PaintingOrSculpture updatedPaintingOrSculpture = paintingOrSculptureRepository.findById(paintingOrSculpture.getId()).get();
        updatedPaintingOrSculpture.materials(UPDATED_MATERIALS).techniques(UPDATED_TECHNIQUES).size(UPDATED_SIZE).place(UPDATED_PLACE);

        restPaintingOrSculptureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaintingOrSculpture.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPaintingOrSculpture))
            )
            .andExpect(status().isOk());

        // Validate the PaintingOrSculpture in the database
        List<PaintingOrSculpture> paintingOrSculptureList = paintingOrSculptureRepository.findAll();
        assertThat(paintingOrSculptureList).hasSize(databaseSizeBeforeUpdate);
        PaintingOrSculpture testPaintingOrSculpture = paintingOrSculptureList.get(paintingOrSculptureList.size() - 1);
        assertThat(testPaintingOrSculpture.getMaterials()).isEqualTo(UPDATED_MATERIALS);
        assertThat(testPaintingOrSculpture.getTechniques()).isEqualTo(UPDATED_TECHNIQUES);
        assertThat(testPaintingOrSculpture.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testPaintingOrSculpture.getPlace()).isEqualTo(UPDATED_PLACE);
    }

    @Test
    void putNonExistingPaintingOrSculpture() throws Exception {
        int databaseSizeBeforeUpdate = paintingOrSculptureRepository.findAll().size();
        paintingOrSculpture.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaintingOrSculptureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paintingOrSculpture.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paintingOrSculpture))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaintingOrSculpture in the database
        List<PaintingOrSculpture> paintingOrSculptureList = paintingOrSculptureRepository.findAll();
        assertThat(paintingOrSculptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPaintingOrSculpture() throws Exception {
        int databaseSizeBeforeUpdate = paintingOrSculptureRepository.findAll().size();
        paintingOrSculpture.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaintingOrSculptureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paintingOrSculpture))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaintingOrSculpture in the database
        List<PaintingOrSculpture> paintingOrSculptureList = paintingOrSculptureRepository.findAll();
        assertThat(paintingOrSculptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPaintingOrSculpture() throws Exception {
        int databaseSizeBeforeUpdate = paintingOrSculptureRepository.findAll().size();
        paintingOrSculpture.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaintingOrSculptureMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paintingOrSculpture))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaintingOrSculpture in the database
        List<PaintingOrSculpture> paintingOrSculptureList = paintingOrSculptureRepository.findAll();
        assertThat(paintingOrSculptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePaintingOrSculptureWithPatch() throws Exception {
        // Initialize the database
        paintingOrSculptureRepository.save(paintingOrSculpture);

        int databaseSizeBeforeUpdate = paintingOrSculptureRepository.findAll().size();

        // Update the paintingOrSculpture using partial update
        PaintingOrSculpture partialUpdatedPaintingOrSculpture = new PaintingOrSculpture();
        partialUpdatedPaintingOrSculpture.setId(paintingOrSculpture.getId());

        partialUpdatedPaintingOrSculpture.materials(UPDATED_MATERIALS).techniques(UPDATED_TECHNIQUES).size(UPDATED_SIZE);

        restPaintingOrSculptureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaintingOrSculpture.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaintingOrSculpture))
            )
            .andExpect(status().isOk());

        // Validate the PaintingOrSculpture in the database
        List<PaintingOrSculpture> paintingOrSculptureList = paintingOrSculptureRepository.findAll();
        assertThat(paintingOrSculptureList).hasSize(databaseSizeBeforeUpdate);
        PaintingOrSculpture testPaintingOrSculpture = paintingOrSculptureList.get(paintingOrSculptureList.size() - 1);
        assertThat(testPaintingOrSculpture.getMaterials()).isEqualTo(UPDATED_MATERIALS);
        assertThat(testPaintingOrSculpture.getTechniques()).isEqualTo(UPDATED_TECHNIQUES);
        assertThat(testPaintingOrSculpture.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testPaintingOrSculpture.getPlace()).isEqualTo(DEFAULT_PLACE);
    }

    @Test
    void fullUpdatePaintingOrSculptureWithPatch() throws Exception {
        // Initialize the database
        paintingOrSculptureRepository.save(paintingOrSculpture);

        int databaseSizeBeforeUpdate = paintingOrSculptureRepository.findAll().size();

        // Update the paintingOrSculpture using partial update
        PaintingOrSculpture partialUpdatedPaintingOrSculpture = new PaintingOrSculpture();
        partialUpdatedPaintingOrSculpture.setId(paintingOrSculpture.getId());

        partialUpdatedPaintingOrSculpture
            .materials(UPDATED_MATERIALS)
            .techniques(UPDATED_TECHNIQUES)
            .size(UPDATED_SIZE)
            .place(UPDATED_PLACE);

        restPaintingOrSculptureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaintingOrSculpture.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaintingOrSculpture))
            )
            .andExpect(status().isOk());

        // Validate the PaintingOrSculpture in the database
        List<PaintingOrSculpture> paintingOrSculptureList = paintingOrSculptureRepository.findAll();
        assertThat(paintingOrSculptureList).hasSize(databaseSizeBeforeUpdate);
        PaintingOrSculpture testPaintingOrSculpture = paintingOrSculptureList.get(paintingOrSculptureList.size() - 1);
        assertThat(testPaintingOrSculpture.getMaterials()).isEqualTo(UPDATED_MATERIALS);
        assertThat(testPaintingOrSculpture.getTechniques()).isEqualTo(UPDATED_TECHNIQUES);
        assertThat(testPaintingOrSculpture.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testPaintingOrSculpture.getPlace()).isEqualTo(UPDATED_PLACE);
    }

    @Test
    void patchNonExistingPaintingOrSculpture() throws Exception {
        int databaseSizeBeforeUpdate = paintingOrSculptureRepository.findAll().size();
        paintingOrSculpture.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaintingOrSculptureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paintingOrSculpture.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paintingOrSculpture))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaintingOrSculpture in the database
        List<PaintingOrSculpture> paintingOrSculptureList = paintingOrSculptureRepository.findAll();
        assertThat(paintingOrSculptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPaintingOrSculpture() throws Exception {
        int databaseSizeBeforeUpdate = paintingOrSculptureRepository.findAll().size();
        paintingOrSculpture.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaintingOrSculptureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paintingOrSculpture))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaintingOrSculpture in the database
        List<PaintingOrSculpture> paintingOrSculptureList = paintingOrSculptureRepository.findAll();
        assertThat(paintingOrSculptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPaintingOrSculpture() throws Exception {
        int databaseSizeBeforeUpdate = paintingOrSculptureRepository.findAll().size();
        paintingOrSculpture.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaintingOrSculptureMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paintingOrSculpture))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaintingOrSculpture in the database
        List<PaintingOrSculpture> paintingOrSculptureList = paintingOrSculptureRepository.findAll();
        assertThat(paintingOrSculptureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePaintingOrSculpture() throws Exception {
        // Initialize the database
        paintingOrSculptureRepository.save(paintingOrSculpture);

        int databaseSizeBeforeDelete = paintingOrSculptureRepository.findAll().size();

        // Delete the paintingOrSculpture
        restPaintingOrSculptureMockMvc
            .perform(delete(ENTITY_API_URL_ID, paintingOrSculpture.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaintingOrSculpture> paintingOrSculptureList = paintingOrSculptureRepository.findAll();
        assertThat(paintingOrSculptureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
