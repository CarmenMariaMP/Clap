package com.clap.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.clap.myapp.IntegrationTest;
import com.clap.myapp.domain.General;
import com.clap.myapp.repository.GeneralRepository;
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
 * Integration tests for the {@link GeneralResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GeneralResourceIT {

    private static final String ENTITY_API_URL = "/api/generals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private GeneralRepository generalRepository;

    @Autowired
    private MockMvc restGeneralMockMvc;

    private General general;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static General createEntity() {
        General general = new General();
        return general;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static General createUpdatedEntity() {
        General general = new General();
        return general;
    }

    @BeforeEach
    public void initTest() {
        generalRepository.deleteAll();
        general = createEntity();
    }

    @Test
    void createGeneral() throws Exception {
        int databaseSizeBeforeCreate = generalRepository.findAll().size();
        // Create the General
        restGeneralMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(general))
            )
            .andExpect(status().isCreated());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeCreate + 1);
        General testGeneral = generalList.get(generalList.size() - 1);
    }

    @Test
    void createGeneralWithExistingId() throws Exception {
        // Create the General with an existing ID
        general.setId("existing_id");

        int databaseSizeBeforeCreate = generalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeneralMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(general))
            )
            .andExpect(status().isBadRequest());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllGenerals() throws Exception {
        // Initialize the database
        generalRepository.save(general);

        // Get all the generalList
        restGeneralMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void getGeneral() throws Exception {
        // Initialize the database
        generalRepository.save(general);

        // Get the general
        restGeneralMockMvc
            .perform(get(ENTITY_API_URL_ID, general.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void getNonExistingGeneral() throws Exception {
        // Get the general
        restGeneralMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewGeneral() throws Exception {
        // Initialize the database
        generalRepository.save(general);

        int databaseSizeBeforeUpdate = generalRepository.findAll().size();

        // Update the general
        General updatedGeneral = generalRepository.findById(general.getId()).get();

        restGeneralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGeneral.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGeneral))
            )
            .andExpect(status().isOk());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
        General testGeneral = generalList.get(generalList.size() - 1);
    }

    @Test
    void putNonExistingGeneral() throws Exception {
        int databaseSizeBeforeUpdate = generalRepository.findAll().size();
        general.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, general.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(general))
            )
            .andExpect(status().isBadRequest());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchGeneral() throws Exception {
        int databaseSizeBeforeUpdate = generalRepository.findAll().size();
        general.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(general))
            )
            .andExpect(status().isBadRequest());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamGeneral() throws Exception {
        int databaseSizeBeforeUpdate = generalRepository.findAll().size();
        general.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneralMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(general))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateGeneralWithPatch() throws Exception {
        // Initialize the database
        generalRepository.save(general);

        int databaseSizeBeforeUpdate = generalRepository.findAll().size();

        // Update the general using partial update
        General partialUpdatedGeneral = new General();
        partialUpdatedGeneral.setId(general.getId());

        restGeneralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeneral.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGeneral))
            )
            .andExpect(status().isOk());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
        General testGeneral = generalList.get(generalList.size() - 1);
    }

    @Test
    void fullUpdateGeneralWithPatch() throws Exception {
        // Initialize the database
        generalRepository.save(general);

        int databaseSizeBeforeUpdate = generalRepository.findAll().size();

        // Update the general using partial update
        General partialUpdatedGeneral = new General();
        partialUpdatedGeneral.setId(general.getId());

        restGeneralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeneral.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGeneral))
            )
            .andExpect(status().isOk());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
        General testGeneral = generalList.get(generalList.size() - 1);
    }

    @Test
    void patchNonExistingGeneral() throws Exception {
        int databaseSizeBeforeUpdate = generalRepository.findAll().size();
        general.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, general.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(general))
            )
            .andExpect(status().isBadRequest());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchGeneral() throws Exception {
        int databaseSizeBeforeUpdate = generalRepository.findAll().size();
        general.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(general))
            )
            .andExpect(status().isBadRequest());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamGeneral() throws Exception {
        int databaseSizeBeforeUpdate = generalRepository.findAll().size();
        general.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneralMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(general))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the General in the database
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteGeneral() throws Exception {
        // Initialize the database
        generalRepository.save(general);

        int databaseSizeBeforeDelete = generalRepository.findAll().size();

        // Delete the general
        restGeneralMockMvc
            .perform(delete(ENTITY_API_URL_ID, general.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<General> generalList = generalRepository.findAll();
        assertThat(generalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
