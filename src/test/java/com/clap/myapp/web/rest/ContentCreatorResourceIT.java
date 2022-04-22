package com.clap.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.clap.myapp.IntegrationTest;
import com.clap.myapp.domain.ContentCreator;
import com.clap.myapp.repository.ContentCreatorRepository;
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
 * Integration tests for the {@link ContentCreatorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContentCreatorResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/content-creators";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ContentCreatorRepository contentCreatorRepository;

    @Autowired
    private MockMvc restContentCreatorMockMvc;

    private ContentCreator contentCreator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentCreator createEntity() {
        ContentCreator contentCreator = new ContentCreator().fullName(DEFAULT_FULL_NAME).country(DEFAULT_COUNTRY).city(DEFAULT_CITY);
        return contentCreator;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentCreator createUpdatedEntity() {
        ContentCreator contentCreator = new ContentCreator().fullName(UPDATED_FULL_NAME).country(UPDATED_COUNTRY).city(UPDATED_CITY);
        return contentCreator;
    }

    @BeforeEach
    public void initTest() {
        contentCreatorRepository.deleteAll();
        contentCreator = createEntity();
    }

    @Test
    void createContentCreator() throws Exception {
        int databaseSizeBeforeCreate = contentCreatorRepository.findAll().size();
        // Create the ContentCreator
        restContentCreatorMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentCreator))
            )
            .andExpect(status().isCreated());

        // Validate the ContentCreator in the database
        List<ContentCreator> contentCreatorList = contentCreatorRepository.findAll();
        assertThat(contentCreatorList).hasSize(databaseSizeBeforeCreate + 1);
        ContentCreator testContentCreator = contentCreatorList.get(contentCreatorList.size() - 1);
        assertThat(testContentCreator.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testContentCreator.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testContentCreator.getCity()).isEqualTo(DEFAULT_CITY);
    }

    @Test
    void createContentCreatorWithExistingId() throws Exception {
        // Create the ContentCreator with an existing ID
        contentCreator.setId("existing_id");

        int databaseSizeBeforeCreate = contentCreatorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContentCreatorMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentCreator))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentCreator in the database
        List<ContentCreator> contentCreatorList = contentCreatorRepository.findAll();
        assertThat(contentCreatorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllContentCreators() throws Exception {
        // Initialize the database
        contentCreatorRepository.save(contentCreator);

        // Get all the contentCreatorList
        restContentCreatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)));
    }

    @Test
    void getContentCreator() throws Exception {
        // Initialize the database
        contentCreatorRepository.save(contentCreator);

        // Get the contentCreator
        restContentCreatorMockMvc
            .perform(get(ENTITY_API_URL_ID, contentCreator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY));
    }

    @Test
    void getNonExistingContentCreator() throws Exception {
        // Get the contentCreator
        restContentCreatorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewContentCreator() throws Exception {
        // Initialize the database
        contentCreatorRepository.save(contentCreator);

        int databaseSizeBeforeUpdate = contentCreatorRepository.findAll().size();

        // Update the contentCreator
        ContentCreator updatedContentCreator = contentCreatorRepository.findById(contentCreator.getId()).get();
        updatedContentCreator.fullName(UPDATED_FULL_NAME).country(UPDATED_COUNTRY).city(UPDATED_CITY);

        restContentCreatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContentCreator.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContentCreator))
            )
            .andExpect(status().isOk());

        // Validate the ContentCreator in the database
        List<ContentCreator> contentCreatorList = contentCreatorRepository.findAll();
        assertThat(contentCreatorList).hasSize(databaseSizeBeforeUpdate);
        ContentCreator testContentCreator = contentCreatorList.get(contentCreatorList.size() - 1);
        assertThat(testContentCreator.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testContentCreator.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testContentCreator.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    void putNonExistingContentCreator() throws Exception {
        int databaseSizeBeforeUpdate = contentCreatorRepository.findAll().size();
        contentCreator.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentCreatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contentCreator.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentCreator))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentCreator in the database
        List<ContentCreator> contentCreatorList = contentCreatorRepository.findAll();
        assertThat(contentCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchContentCreator() throws Exception {
        int databaseSizeBeforeUpdate = contentCreatorRepository.findAll().size();
        contentCreator.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentCreatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentCreator))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentCreator in the database
        List<ContentCreator> contentCreatorList = contentCreatorRepository.findAll();
        assertThat(contentCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamContentCreator() throws Exception {
        int databaseSizeBeforeUpdate = contentCreatorRepository.findAll().size();
        contentCreator.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentCreatorMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentCreator))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContentCreator in the database
        List<ContentCreator> contentCreatorList = contentCreatorRepository.findAll();
        assertThat(contentCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateContentCreatorWithPatch() throws Exception {
        // Initialize the database
        contentCreatorRepository.save(contentCreator);

        int databaseSizeBeforeUpdate = contentCreatorRepository.findAll().size();

        // Update the contentCreator using partial update
        ContentCreator partialUpdatedContentCreator = new ContentCreator();
        partialUpdatedContentCreator.setId(contentCreator.getId());

        partialUpdatedContentCreator.fullName(UPDATED_FULL_NAME).country(UPDATED_COUNTRY);

        restContentCreatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContentCreator.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContentCreator))
            )
            .andExpect(status().isOk());

        // Validate the ContentCreator in the database
        List<ContentCreator> contentCreatorList = contentCreatorRepository.findAll();
        assertThat(contentCreatorList).hasSize(databaseSizeBeforeUpdate);
        ContentCreator testContentCreator = contentCreatorList.get(contentCreatorList.size() - 1);
        assertThat(testContentCreator.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testContentCreator.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testContentCreator.getCity()).isEqualTo(DEFAULT_CITY);
    }

    @Test
    void fullUpdateContentCreatorWithPatch() throws Exception {
        // Initialize the database
        contentCreatorRepository.save(contentCreator);

        int databaseSizeBeforeUpdate = contentCreatorRepository.findAll().size();

        // Update the contentCreator using partial update
        ContentCreator partialUpdatedContentCreator = new ContentCreator();
        partialUpdatedContentCreator.setId(contentCreator.getId());

        partialUpdatedContentCreator.fullName(UPDATED_FULL_NAME).country(UPDATED_COUNTRY).city(UPDATED_CITY);

        restContentCreatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContentCreator.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContentCreator))
            )
            .andExpect(status().isOk());

        // Validate the ContentCreator in the database
        List<ContentCreator> contentCreatorList = contentCreatorRepository.findAll();
        assertThat(contentCreatorList).hasSize(databaseSizeBeforeUpdate);
        ContentCreator testContentCreator = contentCreatorList.get(contentCreatorList.size() - 1);
        assertThat(testContentCreator.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testContentCreator.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testContentCreator.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    void patchNonExistingContentCreator() throws Exception {
        int databaseSizeBeforeUpdate = contentCreatorRepository.findAll().size();
        contentCreator.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentCreatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contentCreator.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contentCreator))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentCreator in the database
        List<ContentCreator> contentCreatorList = contentCreatorRepository.findAll();
        assertThat(contentCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchContentCreator() throws Exception {
        int databaseSizeBeforeUpdate = contentCreatorRepository.findAll().size();
        contentCreator.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentCreatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contentCreator))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentCreator in the database
        List<ContentCreator> contentCreatorList = contentCreatorRepository.findAll();
        assertThat(contentCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamContentCreator() throws Exception {
        int databaseSizeBeforeUpdate = contentCreatorRepository.findAll().size();
        contentCreator.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentCreatorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contentCreator))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContentCreator in the database
        List<ContentCreator> contentCreatorList = contentCreatorRepository.findAll();
        assertThat(contentCreatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteContentCreator() throws Exception {
        // Initialize the database
        contentCreatorRepository.save(contentCreator);

        int databaseSizeBeforeDelete = contentCreatorRepository.findAll().size();

        // Delete the contentCreator
        restContentCreatorMockMvc
            .perform(delete(ENTITY_API_URL_ID, contentCreator.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContentCreator> contentCreatorList = contentCreatorRepository.findAll();
        assertThat(contentCreatorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
