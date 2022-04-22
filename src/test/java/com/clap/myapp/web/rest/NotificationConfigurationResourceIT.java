package com.clap.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.clap.myapp.IntegrationTest;
import com.clap.myapp.domain.NotificationConfiguration;
import com.clap.myapp.repository.NotificationConfigurationRepository;
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
 * Integration tests for the {@link NotificationConfigurationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificationConfigurationResourceIT {

    private static final Boolean DEFAULT_BY_COMMENTS = false;
    private static final Boolean UPDATED_BY_COMMENTS = true;

    private static final Boolean DEFAULT_BY_LIKES = false;
    private static final Boolean UPDATED_BY_LIKES = true;

    private static final Boolean DEFAULT_BY_SAVINGS = false;
    private static final Boolean UPDATED_BY_SAVINGS = true;

    private static final Boolean DEFAULT_BY_SUBSCRIPTIONS = false;
    private static final Boolean UPDATED_BY_SUBSCRIPTIONS = true;

    private static final Boolean DEFAULT_BY_PRIVACY_REQUESTS = false;
    private static final Boolean UPDATED_BY_PRIVACY_REQUESTS = true;

    private static final Boolean DEFAULT_BY_PRIVACY_REQUESTS_STATUS = false;
    private static final Boolean UPDATED_BY_PRIVACY_REQUESTS_STATUS = true;

    private static final String ENTITY_API_URL = "/api/notification-configurations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private NotificationConfigurationRepository notificationConfigurationRepository;

    @Autowired
    private MockMvc restNotificationConfigurationMockMvc;

    private NotificationConfiguration notificationConfiguration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationConfiguration createEntity() {
        NotificationConfiguration notificationConfiguration = new NotificationConfiguration()
            .byComments(DEFAULT_BY_COMMENTS)
            .byLikes(DEFAULT_BY_LIKES)
            .bySavings(DEFAULT_BY_SAVINGS)
            .bySubscriptions(DEFAULT_BY_SUBSCRIPTIONS)
            .byPrivacyRequests(DEFAULT_BY_PRIVACY_REQUESTS)
            .byPrivacyRequestsStatus(DEFAULT_BY_PRIVACY_REQUESTS_STATUS);
        return notificationConfiguration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationConfiguration createUpdatedEntity() {
        NotificationConfiguration notificationConfiguration = new NotificationConfiguration()
            .byComments(UPDATED_BY_COMMENTS)
            .byLikes(UPDATED_BY_LIKES)
            .bySavings(UPDATED_BY_SAVINGS)
            .bySubscriptions(UPDATED_BY_SUBSCRIPTIONS)
            .byPrivacyRequests(UPDATED_BY_PRIVACY_REQUESTS)
            .byPrivacyRequestsStatus(UPDATED_BY_PRIVACY_REQUESTS_STATUS);
        return notificationConfiguration;
    }

    @BeforeEach
    public void initTest() {
        notificationConfigurationRepository.deleteAll();
        notificationConfiguration = createEntity();
    }

    @Test
    void createNotificationConfiguration() throws Exception {
        int databaseSizeBeforeCreate = notificationConfigurationRepository.findAll().size();
        // Create the NotificationConfiguration
        restNotificationConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationConfiguration))
            )
            .andExpect(status().isCreated());

        // Validate the NotificationConfiguration in the database
        List<NotificationConfiguration> notificationConfigurationList = notificationConfigurationRepository.findAll();
        assertThat(notificationConfigurationList).hasSize(databaseSizeBeforeCreate + 1);
        NotificationConfiguration testNotificationConfiguration = notificationConfigurationList.get(
            notificationConfigurationList.size() - 1
        );
        assertThat(testNotificationConfiguration.getByComments()).isEqualTo(DEFAULT_BY_COMMENTS);
        assertThat(testNotificationConfiguration.getByLikes()).isEqualTo(DEFAULT_BY_LIKES);
        assertThat(testNotificationConfiguration.getBySavings()).isEqualTo(DEFAULT_BY_SAVINGS);
        assertThat(testNotificationConfiguration.getBySubscriptions()).isEqualTo(DEFAULT_BY_SUBSCRIPTIONS);
        assertThat(testNotificationConfiguration.getByPrivacyRequests()).isEqualTo(DEFAULT_BY_PRIVACY_REQUESTS);
        assertThat(testNotificationConfiguration.getByPrivacyRequestsStatus()).isEqualTo(DEFAULT_BY_PRIVACY_REQUESTS_STATUS);
    }

    @Test
    void createNotificationConfigurationWithExistingId() throws Exception {
        // Create the NotificationConfiguration with an existing ID
        notificationConfiguration.setId("existing_id");

        int databaseSizeBeforeCreate = notificationConfigurationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationConfiguration in the database
        List<NotificationConfiguration> notificationConfigurationList = notificationConfigurationRepository.findAll();
        assertThat(notificationConfigurationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllNotificationConfigurations() throws Exception {
        // Initialize the database
        notificationConfigurationRepository.save(notificationConfiguration);

        // Get all the notificationConfigurationList
        restNotificationConfigurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].byComments").value(hasItem(DEFAULT_BY_COMMENTS.booleanValue())))
            .andExpect(jsonPath("$.[*].byLikes").value(hasItem(DEFAULT_BY_LIKES.booleanValue())))
            .andExpect(jsonPath("$.[*].bySavings").value(hasItem(DEFAULT_BY_SAVINGS.booleanValue())))
            .andExpect(jsonPath("$.[*].bySubscriptions").value(hasItem(DEFAULT_BY_SUBSCRIPTIONS.booleanValue())))
            .andExpect(jsonPath("$.[*].byPrivacyRequests").value(hasItem(DEFAULT_BY_PRIVACY_REQUESTS.booleanValue())))
            .andExpect(jsonPath("$.[*].byPrivacyRequestsStatus").value(hasItem(DEFAULT_BY_PRIVACY_REQUESTS_STATUS.booleanValue())));
    }

    @Test
    void getNotificationConfiguration() throws Exception {
        // Initialize the database
        notificationConfigurationRepository.save(notificationConfiguration);

        // Get the notificationConfiguration
        restNotificationConfigurationMockMvc
            .perform(get(ENTITY_API_URL_ID, notificationConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.byComments").value(DEFAULT_BY_COMMENTS.booleanValue()))
            .andExpect(jsonPath("$.byLikes").value(DEFAULT_BY_LIKES.booleanValue()))
            .andExpect(jsonPath("$.bySavings").value(DEFAULT_BY_SAVINGS.booleanValue()))
            .andExpect(jsonPath("$.bySubscriptions").value(DEFAULT_BY_SUBSCRIPTIONS.booleanValue()))
            .andExpect(jsonPath("$.byPrivacyRequests").value(DEFAULT_BY_PRIVACY_REQUESTS.booleanValue()))
            .andExpect(jsonPath("$.byPrivacyRequestsStatus").value(DEFAULT_BY_PRIVACY_REQUESTS_STATUS.booleanValue()));
    }

    @Test
    void getNonExistingNotificationConfiguration() throws Exception {
        // Get the notificationConfiguration
        restNotificationConfigurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewNotificationConfiguration() throws Exception {
        // Initialize the database
        notificationConfigurationRepository.save(notificationConfiguration);

        int databaseSizeBeforeUpdate = notificationConfigurationRepository.findAll().size();

        // Update the notificationConfiguration
        NotificationConfiguration updatedNotificationConfiguration = notificationConfigurationRepository
            .findById(notificationConfiguration.getId())
            .get();
        updatedNotificationConfiguration
            .byComments(UPDATED_BY_COMMENTS)
            .byLikes(UPDATED_BY_LIKES)
            .bySavings(UPDATED_BY_SAVINGS)
            .bySubscriptions(UPDATED_BY_SUBSCRIPTIONS)
            .byPrivacyRequests(UPDATED_BY_PRIVACY_REQUESTS)
            .byPrivacyRequestsStatus(UPDATED_BY_PRIVACY_REQUESTS_STATUS);

        restNotificationConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNotificationConfiguration.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNotificationConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the NotificationConfiguration in the database
        List<NotificationConfiguration> notificationConfigurationList = notificationConfigurationRepository.findAll();
        assertThat(notificationConfigurationList).hasSize(databaseSizeBeforeUpdate);
        NotificationConfiguration testNotificationConfiguration = notificationConfigurationList.get(
            notificationConfigurationList.size() - 1
        );
        assertThat(testNotificationConfiguration.getByComments()).isEqualTo(UPDATED_BY_COMMENTS);
        assertThat(testNotificationConfiguration.getByLikes()).isEqualTo(UPDATED_BY_LIKES);
        assertThat(testNotificationConfiguration.getBySavings()).isEqualTo(UPDATED_BY_SAVINGS);
        assertThat(testNotificationConfiguration.getBySubscriptions()).isEqualTo(UPDATED_BY_SUBSCRIPTIONS);
        assertThat(testNotificationConfiguration.getByPrivacyRequests()).isEqualTo(UPDATED_BY_PRIVACY_REQUESTS);
        assertThat(testNotificationConfiguration.getByPrivacyRequestsStatus()).isEqualTo(UPDATED_BY_PRIVACY_REQUESTS_STATUS);
    }

    @Test
    void putNonExistingNotificationConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = notificationConfigurationRepository.findAll().size();
        notificationConfiguration.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationConfiguration.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationConfiguration in the database
        List<NotificationConfiguration> notificationConfigurationList = notificationConfigurationRepository.findAll();
        assertThat(notificationConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchNotificationConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = notificationConfigurationRepository.findAll().size();
        notificationConfiguration.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationConfiguration in the database
        List<NotificationConfiguration> notificationConfigurationList = notificationConfigurationRepository.findAll();
        assertThat(notificationConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamNotificationConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = notificationConfigurationRepository.findAll().size();
        notificationConfiguration.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificationConfiguration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationConfiguration in the database
        List<NotificationConfiguration> notificationConfigurationList = notificationConfigurationRepository.findAll();
        assertThat(notificationConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateNotificationConfigurationWithPatch() throws Exception {
        // Initialize the database
        notificationConfigurationRepository.save(notificationConfiguration);

        int databaseSizeBeforeUpdate = notificationConfigurationRepository.findAll().size();

        // Update the notificationConfiguration using partial update
        NotificationConfiguration partialUpdatedNotificationConfiguration = new NotificationConfiguration();
        partialUpdatedNotificationConfiguration.setId(notificationConfiguration.getId());

        partialUpdatedNotificationConfiguration.byLikes(UPDATED_BY_LIKES).bySubscriptions(UPDATED_BY_SUBSCRIPTIONS);

        restNotificationConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationConfiguration.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotificationConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the NotificationConfiguration in the database
        List<NotificationConfiguration> notificationConfigurationList = notificationConfigurationRepository.findAll();
        assertThat(notificationConfigurationList).hasSize(databaseSizeBeforeUpdate);
        NotificationConfiguration testNotificationConfiguration = notificationConfigurationList.get(
            notificationConfigurationList.size() - 1
        );
        assertThat(testNotificationConfiguration.getByComments()).isEqualTo(DEFAULT_BY_COMMENTS);
        assertThat(testNotificationConfiguration.getByLikes()).isEqualTo(UPDATED_BY_LIKES);
        assertThat(testNotificationConfiguration.getBySavings()).isEqualTo(DEFAULT_BY_SAVINGS);
        assertThat(testNotificationConfiguration.getBySubscriptions()).isEqualTo(UPDATED_BY_SUBSCRIPTIONS);
        assertThat(testNotificationConfiguration.getByPrivacyRequests()).isEqualTo(DEFAULT_BY_PRIVACY_REQUESTS);
        assertThat(testNotificationConfiguration.getByPrivacyRequestsStatus()).isEqualTo(DEFAULT_BY_PRIVACY_REQUESTS_STATUS);
    }

    @Test
    void fullUpdateNotificationConfigurationWithPatch() throws Exception {
        // Initialize the database
        notificationConfigurationRepository.save(notificationConfiguration);

        int databaseSizeBeforeUpdate = notificationConfigurationRepository.findAll().size();

        // Update the notificationConfiguration using partial update
        NotificationConfiguration partialUpdatedNotificationConfiguration = new NotificationConfiguration();
        partialUpdatedNotificationConfiguration.setId(notificationConfiguration.getId());

        partialUpdatedNotificationConfiguration
            .byComments(UPDATED_BY_COMMENTS)
            .byLikes(UPDATED_BY_LIKES)
            .bySavings(UPDATED_BY_SAVINGS)
            .bySubscriptions(UPDATED_BY_SUBSCRIPTIONS)
            .byPrivacyRequests(UPDATED_BY_PRIVACY_REQUESTS)
            .byPrivacyRequestsStatus(UPDATED_BY_PRIVACY_REQUESTS_STATUS);

        restNotificationConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationConfiguration.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotificationConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the NotificationConfiguration in the database
        List<NotificationConfiguration> notificationConfigurationList = notificationConfigurationRepository.findAll();
        assertThat(notificationConfigurationList).hasSize(databaseSizeBeforeUpdate);
        NotificationConfiguration testNotificationConfiguration = notificationConfigurationList.get(
            notificationConfigurationList.size() - 1
        );
        assertThat(testNotificationConfiguration.getByComments()).isEqualTo(UPDATED_BY_COMMENTS);
        assertThat(testNotificationConfiguration.getByLikes()).isEqualTo(UPDATED_BY_LIKES);
        assertThat(testNotificationConfiguration.getBySavings()).isEqualTo(UPDATED_BY_SAVINGS);
        assertThat(testNotificationConfiguration.getBySubscriptions()).isEqualTo(UPDATED_BY_SUBSCRIPTIONS);
        assertThat(testNotificationConfiguration.getByPrivacyRequests()).isEqualTo(UPDATED_BY_PRIVACY_REQUESTS);
        assertThat(testNotificationConfiguration.getByPrivacyRequestsStatus()).isEqualTo(UPDATED_BY_PRIVACY_REQUESTS_STATUS);
    }

    @Test
    void patchNonExistingNotificationConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = notificationConfigurationRepository.findAll().size();
        notificationConfiguration.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificationConfiguration.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificationConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationConfiguration in the database
        List<NotificationConfiguration> notificationConfigurationList = notificationConfigurationRepository.findAll();
        assertThat(notificationConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchNotificationConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = notificationConfigurationRepository.findAll().size();
        notificationConfiguration.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificationConfiguration))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationConfiguration in the database
        List<NotificationConfiguration> notificationConfigurationList = notificationConfigurationRepository.findAll();
        assertThat(notificationConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamNotificationConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = notificationConfigurationRepository.findAll().size();
        notificationConfiguration.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificationConfiguration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationConfiguration in the database
        List<NotificationConfiguration> notificationConfigurationList = notificationConfigurationRepository.findAll();
        assertThat(notificationConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteNotificationConfiguration() throws Exception {
        // Initialize the database
        notificationConfigurationRepository.save(notificationConfiguration);

        int databaseSizeBeforeDelete = notificationConfigurationRepository.findAll().size();

        // Delete the notificationConfiguration
        restNotificationConfigurationMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificationConfiguration.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NotificationConfiguration> notificationConfigurationList = notificationConfigurationRepository.findAll();
        assertThat(notificationConfigurationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
