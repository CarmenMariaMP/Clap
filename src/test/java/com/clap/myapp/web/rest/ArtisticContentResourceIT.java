package com.clap.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.clap.myapp.IntegrationTest;
import com.clap.myapp.domain.ArtisticContent;
import com.clap.myapp.repository.ArtisticContentRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ArtisticContentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArtisticContentResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_URL = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_URL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPLOAD_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPLOAD_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_VIEW_COUNT = 1L;
    private static final Long UPDATED_VIEW_COUNT = 2L;

    private static final String ENTITY_API_URL = "/api/artistic-contents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ArtisticContentRepository artisticContentRepository;

    @Autowired
    private MockMvc restArtisticContentMockMvc;

    private ArtisticContent artisticContent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArtisticContent createEntity() {
        ArtisticContent artisticContent = new ArtisticContent()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .contentUrl(DEFAULT_CONTENT_URL)
            .uploadDate(DEFAULT_UPLOAD_DATE)
            .viewCount(DEFAULT_VIEW_COUNT);
        return artisticContent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArtisticContent createUpdatedEntity() {
        ArtisticContent artisticContent = new ArtisticContent()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .contentUrl(UPDATED_CONTENT_URL)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .viewCount(UPDATED_VIEW_COUNT);
        return artisticContent;
    }

    @BeforeEach
    public void initTest() {
        artisticContentRepository.deleteAll();
        artisticContent = createEntity();
    }

    @Test
    void createArtisticContent() throws Exception {
        int databaseSizeBeforeCreate = artisticContentRepository.findAll().size();
        // Create the ArtisticContent
        restArtisticContentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(artisticContent))
            )
            .andExpect(status().isCreated());

        // Validate the ArtisticContent in the database
        List<ArtisticContent> artisticContentList = artisticContentRepository.findAll();
        assertThat(artisticContentList).hasSize(databaseSizeBeforeCreate + 1);
        ArtisticContent testArtisticContent = artisticContentList.get(artisticContentList.size() - 1);
        assertThat(testArtisticContent.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testArtisticContent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testArtisticContent.getContentUrl()).isEqualTo(DEFAULT_CONTENT_URL);
        assertThat(testArtisticContent.getUploadDate()).isEqualTo(DEFAULT_UPLOAD_DATE);
        assertThat(testArtisticContent.getViewCount()).isEqualTo(DEFAULT_VIEW_COUNT);
    }

    @Test
    void createArtisticContentWithExistingId() throws Exception {
        // Create the ArtisticContent with an existing ID
        artisticContent.setId("existing_id");

        int databaseSizeBeforeCreate = artisticContentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtisticContentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(artisticContent))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArtisticContent in the database
        List<ArtisticContent> artisticContentList = artisticContentRepository.findAll();
        assertThat(artisticContentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllArtisticContents() throws Exception {
        // Initialize the database
        artisticContentRepository.save(artisticContent);

        // Get all the artisticContentList
        restArtisticContentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].contentUrl").value(hasItem(DEFAULT_CONTENT_URL)))
            .andExpect(jsonPath("$.[*].uploadDate").value(hasItem(DEFAULT_UPLOAD_DATE.toString())))
            .andExpect(jsonPath("$.[*].viewCount").value(hasItem(DEFAULT_VIEW_COUNT.intValue())));
    }

    @Test
    void getArtisticContent() throws Exception {
        // Initialize the database
        artisticContentRepository.save(artisticContent);

        // Get the artisticContent
        restArtisticContentMockMvc
            .perform(get(ENTITY_API_URL_ID, artisticContent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.contentUrl").value(DEFAULT_CONTENT_URL))
            .andExpect(jsonPath("$.uploadDate").value(DEFAULT_UPLOAD_DATE.toString()))
            .andExpect(jsonPath("$.viewCount").value(DEFAULT_VIEW_COUNT.intValue()));
    }

    @Test
    void getNonExistingArtisticContent() throws Exception {
        // Get the artisticContent
        restArtisticContentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewArtisticContent() throws Exception {
        // Initialize the database
        artisticContentRepository.save(artisticContent);

        int databaseSizeBeforeUpdate = artisticContentRepository.findAll().size();

        // Update the artisticContent
        ArtisticContent updatedArtisticContent = artisticContentRepository.findById(artisticContent.getId()).get();
        updatedArtisticContent
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .contentUrl(UPDATED_CONTENT_URL)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .viewCount(UPDATED_VIEW_COUNT);

        restArtisticContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedArtisticContent.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedArtisticContent))
            )
            .andExpect(status().isOk());

        // Validate the ArtisticContent in the database
        List<ArtisticContent> artisticContentList = artisticContentRepository.findAll();
        assertThat(artisticContentList).hasSize(databaseSizeBeforeUpdate);
        ArtisticContent testArtisticContent = artisticContentList.get(artisticContentList.size() - 1);
        assertThat(testArtisticContent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArtisticContent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testArtisticContent.getContentUrl()).isEqualTo(UPDATED_CONTENT_URL);
        assertThat(testArtisticContent.getUploadDate()).isEqualTo(UPDATED_UPLOAD_DATE);
        assertThat(testArtisticContent.getViewCount()).isEqualTo(UPDATED_VIEW_COUNT);
    }

    @Test
    void putNonExistingArtisticContent() throws Exception {
        int databaseSizeBeforeUpdate = artisticContentRepository.findAll().size();
        artisticContent.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtisticContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, artisticContent.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(artisticContent))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArtisticContent in the database
        List<ArtisticContent> artisticContentList = artisticContentRepository.findAll();
        assertThat(artisticContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchArtisticContent() throws Exception {
        int databaseSizeBeforeUpdate = artisticContentRepository.findAll().size();
        artisticContent.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArtisticContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(artisticContent))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArtisticContent in the database
        List<ArtisticContent> artisticContentList = artisticContentRepository.findAll();
        assertThat(artisticContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamArtisticContent() throws Exception {
        int databaseSizeBeforeUpdate = artisticContentRepository.findAll().size();
        artisticContent.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArtisticContentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(artisticContent))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArtisticContent in the database
        List<ArtisticContent> artisticContentList = artisticContentRepository.findAll();
        assertThat(artisticContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateArtisticContentWithPatch() throws Exception {
        // Initialize the database
        artisticContentRepository.save(artisticContent);

        int databaseSizeBeforeUpdate = artisticContentRepository.findAll().size();

        // Update the artisticContent using partial update
        ArtisticContent partialUpdatedArtisticContent = new ArtisticContent();
        partialUpdatedArtisticContent.setId(artisticContent.getId());

        partialUpdatedArtisticContent.contentUrl(UPDATED_CONTENT_URL).uploadDate(UPDATED_UPLOAD_DATE);

        restArtisticContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArtisticContent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArtisticContent))
            )
            .andExpect(status().isOk());

        // Validate the ArtisticContent in the database
        List<ArtisticContent> artisticContentList = artisticContentRepository.findAll();
        assertThat(artisticContentList).hasSize(databaseSizeBeforeUpdate);
        ArtisticContent testArtisticContent = artisticContentList.get(artisticContentList.size() - 1);
        assertThat(testArtisticContent.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testArtisticContent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testArtisticContent.getContentUrl()).isEqualTo(UPDATED_CONTENT_URL);
        assertThat(testArtisticContent.getUploadDate()).isEqualTo(UPDATED_UPLOAD_DATE);
        assertThat(testArtisticContent.getViewCount()).isEqualTo(DEFAULT_VIEW_COUNT);
    }

    @Test
    void fullUpdateArtisticContentWithPatch() throws Exception {
        // Initialize the database
        artisticContentRepository.save(artisticContent);

        int databaseSizeBeforeUpdate = artisticContentRepository.findAll().size();

        // Update the artisticContent using partial update
        ArtisticContent partialUpdatedArtisticContent = new ArtisticContent();
        partialUpdatedArtisticContent.setId(artisticContent.getId());

        partialUpdatedArtisticContent
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .contentUrl(UPDATED_CONTENT_URL)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .viewCount(UPDATED_VIEW_COUNT);

        restArtisticContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArtisticContent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArtisticContent))
            )
            .andExpect(status().isOk());

        // Validate the ArtisticContent in the database
        List<ArtisticContent> artisticContentList = artisticContentRepository.findAll();
        assertThat(artisticContentList).hasSize(databaseSizeBeforeUpdate);
        ArtisticContent testArtisticContent = artisticContentList.get(artisticContentList.size() - 1);
        assertThat(testArtisticContent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArtisticContent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testArtisticContent.getContentUrl()).isEqualTo(UPDATED_CONTENT_URL);
        assertThat(testArtisticContent.getUploadDate()).isEqualTo(UPDATED_UPLOAD_DATE);
        assertThat(testArtisticContent.getViewCount()).isEqualTo(UPDATED_VIEW_COUNT);
    }

    @Test
    void patchNonExistingArtisticContent() throws Exception {
        int databaseSizeBeforeUpdate = artisticContentRepository.findAll().size();
        artisticContent.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtisticContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, artisticContent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(artisticContent))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArtisticContent in the database
        List<ArtisticContent> artisticContentList = artisticContentRepository.findAll();
        assertThat(artisticContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchArtisticContent() throws Exception {
        int databaseSizeBeforeUpdate = artisticContentRepository.findAll().size();
        artisticContent.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArtisticContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(artisticContent))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArtisticContent in the database
        List<ArtisticContent> artisticContentList = artisticContentRepository.findAll();
        assertThat(artisticContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamArtisticContent() throws Exception {
        int databaseSizeBeforeUpdate = artisticContentRepository.findAll().size();
        artisticContent.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArtisticContentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(artisticContent))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArtisticContent in the database
        List<ArtisticContent> artisticContentList = artisticContentRepository.findAll();
        assertThat(artisticContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteArtisticContent() throws Exception {
        // Initialize the database
        artisticContentRepository.save(artisticContent);

        int databaseSizeBeforeDelete = artisticContentRepository.findAll().size();

        // Delete the artisticContent
        restArtisticContentMockMvc
            .perform(delete(ENTITY_API_URL_ID, artisticContent.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArtisticContent> artisticContentList = artisticContentRepository.findAll();
        assertThat(artisticContentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
