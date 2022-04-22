package com.clap.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.clap.myapp.IntegrationTest;
import com.clap.myapp.domain.PrivacyRequest;
import com.clap.myapp.domain.enumeration.RequestStateType;
import com.clap.myapp.repository.PrivacyRequestRepository;
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
 * Integration tests for the {@link PrivacyRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrivacyRequestResourceIT {

    private static final RequestStateType DEFAULT_REQUEST_STATE = RequestStateType.PENDING;
    private static final RequestStateType UPDATED_REQUEST_STATE = RequestStateType.ACCEPTED;

    private static final LocalDate DEFAULT_REQUEST_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REQUEST_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/privacy-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PrivacyRequestRepository privacyRequestRepository;

    @Autowired
    private MockMvc restPrivacyRequestMockMvc;

    private PrivacyRequest privacyRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrivacyRequest createEntity() {
        PrivacyRequest privacyRequest = new PrivacyRequest().requestState(DEFAULT_REQUEST_STATE).requestDate(DEFAULT_REQUEST_DATE);
        return privacyRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrivacyRequest createUpdatedEntity() {
        PrivacyRequest privacyRequest = new PrivacyRequest().requestState(UPDATED_REQUEST_STATE).requestDate(UPDATED_REQUEST_DATE);
        return privacyRequest;
    }

    @BeforeEach
    public void initTest() {
        privacyRequestRepository.deleteAll();
        privacyRequest = createEntity();
    }

    @Test
    void createPrivacyRequest() throws Exception {
        int databaseSizeBeforeCreate = privacyRequestRepository.findAll().size();
        // Create the PrivacyRequest
        restPrivacyRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(privacyRequest))
            )
            .andExpect(status().isCreated());

        // Validate the PrivacyRequest in the database
        List<PrivacyRequest> privacyRequestList = privacyRequestRepository.findAll();
        assertThat(privacyRequestList).hasSize(databaseSizeBeforeCreate + 1);
        PrivacyRequest testPrivacyRequest = privacyRequestList.get(privacyRequestList.size() - 1);
        assertThat(testPrivacyRequest.getRequestState()).isEqualTo(DEFAULT_REQUEST_STATE);
        assertThat(testPrivacyRequest.getRequestDate()).isEqualTo(DEFAULT_REQUEST_DATE);
    }

    @Test
    void createPrivacyRequestWithExistingId() throws Exception {
        // Create the PrivacyRequest with an existing ID
        privacyRequest.setId("existing_id");

        int databaseSizeBeforeCreate = privacyRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrivacyRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(privacyRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrivacyRequest in the database
        List<PrivacyRequest> privacyRequestList = privacyRequestRepository.findAll();
        assertThat(privacyRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPrivacyRequests() throws Exception {
        // Initialize the database
        privacyRequestRepository.save(privacyRequest);

        // Get all the privacyRequestList
        restPrivacyRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].requestState").value(hasItem(DEFAULT_REQUEST_STATE.toString())))
            .andExpect(jsonPath("$.[*].requestDate").value(hasItem(DEFAULT_REQUEST_DATE.toString())));
    }

    @Test
    void getPrivacyRequest() throws Exception {
        // Initialize the database
        privacyRequestRepository.save(privacyRequest);

        // Get the privacyRequest
        restPrivacyRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, privacyRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.requestState").value(DEFAULT_REQUEST_STATE.toString()))
            .andExpect(jsonPath("$.requestDate").value(DEFAULT_REQUEST_DATE.toString()));
    }

    @Test
    void getNonExistingPrivacyRequest() throws Exception {
        // Get the privacyRequest
        restPrivacyRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewPrivacyRequest() throws Exception {
        // Initialize the database
        privacyRequestRepository.save(privacyRequest);

        int databaseSizeBeforeUpdate = privacyRequestRepository.findAll().size();

        // Update the privacyRequest
        PrivacyRequest updatedPrivacyRequest = privacyRequestRepository.findById(privacyRequest.getId()).get();
        updatedPrivacyRequest.requestState(UPDATED_REQUEST_STATE).requestDate(UPDATED_REQUEST_DATE);

        restPrivacyRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrivacyRequest.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrivacyRequest))
            )
            .andExpect(status().isOk());

        // Validate the PrivacyRequest in the database
        List<PrivacyRequest> privacyRequestList = privacyRequestRepository.findAll();
        assertThat(privacyRequestList).hasSize(databaseSizeBeforeUpdate);
        PrivacyRequest testPrivacyRequest = privacyRequestList.get(privacyRequestList.size() - 1);
        assertThat(testPrivacyRequest.getRequestState()).isEqualTo(UPDATED_REQUEST_STATE);
        assertThat(testPrivacyRequest.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
    }

    @Test
    void putNonExistingPrivacyRequest() throws Exception {
        int databaseSizeBeforeUpdate = privacyRequestRepository.findAll().size();
        privacyRequest.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrivacyRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, privacyRequest.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(privacyRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrivacyRequest in the database
        List<PrivacyRequest> privacyRequestList = privacyRequestRepository.findAll();
        assertThat(privacyRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPrivacyRequest() throws Exception {
        int databaseSizeBeforeUpdate = privacyRequestRepository.findAll().size();
        privacyRequest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrivacyRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(privacyRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrivacyRequest in the database
        List<PrivacyRequest> privacyRequestList = privacyRequestRepository.findAll();
        assertThat(privacyRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPrivacyRequest() throws Exception {
        int databaseSizeBeforeUpdate = privacyRequestRepository.findAll().size();
        privacyRequest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrivacyRequestMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(privacyRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrivacyRequest in the database
        List<PrivacyRequest> privacyRequestList = privacyRequestRepository.findAll();
        assertThat(privacyRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePrivacyRequestWithPatch() throws Exception {
        // Initialize the database
        privacyRequestRepository.save(privacyRequest);

        int databaseSizeBeforeUpdate = privacyRequestRepository.findAll().size();

        // Update the privacyRequest using partial update
        PrivacyRequest partialUpdatedPrivacyRequest = new PrivacyRequest();
        partialUpdatedPrivacyRequest.setId(privacyRequest.getId());

        partialUpdatedPrivacyRequest.requestState(UPDATED_REQUEST_STATE).requestDate(UPDATED_REQUEST_DATE);

        restPrivacyRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrivacyRequest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrivacyRequest))
            )
            .andExpect(status().isOk());

        // Validate the PrivacyRequest in the database
        List<PrivacyRequest> privacyRequestList = privacyRequestRepository.findAll();
        assertThat(privacyRequestList).hasSize(databaseSizeBeforeUpdate);
        PrivacyRequest testPrivacyRequest = privacyRequestList.get(privacyRequestList.size() - 1);
        assertThat(testPrivacyRequest.getRequestState()).isEqualTo(UPDATED_REQUEST_STATE);
        assertThat(testPrivacyRequest.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
    }

    @Test
    void fullUpdatePrivacyRequestWithPatch() throws Exception {
        // Initialize the database
        privacyRequestRepository.save(privacyRequest);

        int databaseSizeBeforeUpdate = privacyRequestRepository.findAll().size();

        // Update the privacyRequest using partial update
        PrivacyRequest partialUpdatedPrivacyRequest = new PrivacyRequest();
        partialUpdatedPrivacyRequest.setId(privacyRequest.getId());

        partialUpdatedPrivacyRequest.requestState(UPDATED_REQUEST_STATE).requestDate(UPDATED_REQUEST_DATE);

        restPrivacyRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrivacyRequest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrivacyRequest))
            )
            .andExpect(status().isOk());

        // Validate the PrivacyRequest in the database
        List<PrivacyRequest> privacyRequestList = privacyRequestRepository.findAll();
        assertThat(privacyRequestList).hasSize(databaseSizeBeforeUpdate);
        PrivacyRequest testPrivacyRequest = privacyRequestList.get(privacyRequestList.size() - 1);
        assertThat(testPrivacyRequest.getRequestState()).isEqualTo(UPDATED_REQUEST_STATE);
        assertThat(testPrivacyRequest.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
    }

    @Test
    void patchNonExistingPrivacyRequest() throws Exception {
        int databaseSizeBeforeUpdate = privacyRequestRepository.findAll().size();
        privacyRequest.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrivacyRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, privacyRequest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(privacyRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrivacyRequest in the database
        List<PrivacyRequest> privacyRequestList = privacyRequestRepository.findAll();
        assertThat(privacyRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPrivacyRequest() throws Exception {
        int databaseSizeBeforeUpdate = privacyRequestRepository.findAll().size();
        privacyRequest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrivacyRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(privacyRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrivacyRequest in the database
        List<PrivacyRequest> privacyRequestList = privacyRequestRepository.findAll();
        assertThat(privacyRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPrivacyRequest() throws Exception {
        int databaseSizeBeforeUpdate = privacyRequestRepository.findAll().size();
        privacyRequest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrivacyRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(privacyRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrivacyRequest in the database
        List<PrivacyRequest> privacyRequestList = privacyRequestRepository.findAll();
        assertThat(privacyRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePrivacyRequest() throws Exception {
        // Initialize the database
        privacyRequestRepository.save(privacyRequest);

        int databaseSizeBeforeDelete = privacyRequestRepository.findAll().size();

        // Delete the privacyRequest
        restPrivacyRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, privacyRequest.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PrivacyRequest> privacyRequestList = privacyRequestRepository.findAll();
        assertThat(privacyRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
