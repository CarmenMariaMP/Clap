package com.clap.myapp.web.rest;

import com.clap.myapp.domain.NotificationConfiguration;
import com.clap.myapp.repository.NotificationConfigurationRepository;
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
 * REST controller for managing {@link com.clap.myapp.domain.NotificationConfiguration}.
 */
@RestController
@RequestMapping("/api")
public class NotificationConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationConfigurationResource.class);

    private static final String ENTITY_NAME = "notificationConfiguration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationConfigurationRepository notificationConfigurationRepository;

    public NotificationConfigurationResource(NotificationConfigurationRepository notificationConfigurationRepository) {
        this.notificationConfigurationRepository = notificationConfigurationRepository;
    }

    /**
     * {@code POST  /notification-configurations} : Create a new notificationConfiguration.
     *
     * @param notificationConfiguration the notificationConfiguration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationConfiguration, or with status {@code 400 (Bad Request)} if the notificationConfiguration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notification-configurations")
    public ResponseEntity<NotificationConfiguration> createNotificationConfiguration(
        @RequestBody NotificationConfiguration notificationConfiguration
    ) throws URISyntaxException {
        log.debug("REST request to save NotificationConfiguration : {}", notificationConfiguration);
        if (notificationConfiguration.getId() != null) {
            throw new BadRequestAlertException("A new notificationConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotificationConfiguration result = notificationConfigurationRepository.save(notificationConfiguration);
        return ResponseEntity
            .created(new URI("/api/notification-configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /notification-configurations/:id} : Updates an existing notificationConfiguration.
     *
     * @param id the id of the notificationConfiguration to save.
     * @param notificationConfiguration the notificationConfiguration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationConfiguration,
     * or with status {@code 400 (Bad Request)} if the notificationConfiguration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationConfiguration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notification-configurations/{id}")
    public ResponseEntity<NotificationConfiguration> updateNotificationConfiguration(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody NotificationConfiguration notificationConfiguration
    ) throws URISyntaxException {
        log.debug("REST request to update NotificationConfiguration : {}, {}", id, notificationConfiguration);
        if (notificationConfiguration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationConfiguration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NotificationConfiguration result = notificationConfigurationRepository.save(notificationConfiguration);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationConfiguration.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /notification-configurations/:id} : Partial updates given fields of an existing notificationConfiguration, field will ignore if it is null
     *
     * @param id the id of the notificationConfiguration to save.
     * @param notificationConfiguration the notificationConfiguration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationConfiguration,
     * or with status {@code 400 (Bad Request)} if the notificationConfiguration is not valid,
     * or with status {@code 404 (Not Found)} if the notificationConfiguration is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificationConfiguration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/notification-configurations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NotificationConfiguration> partialUpdateNotificationConfiguration(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody NotificationConfiguration notificationConfiguration
    ) throws URISyntaxException {
        log.debug("REST request to partial update NotificationConfiguration partially : {}, {}", id, notificationConfiguration);
        if (notificationConfiguration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationConfiguration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotificationConfiguration> result = notificationConfigurationRepository
            .findById(notificationConfiguration.getId())
            .map(existingNotificationConfiguration -> {
                if (notificationConfiguration.getByComments() != null) {
                    existingNotificationConfiguration.setByComments(notificationConfiguration.getByComments());
                }
                if (notificationConfiguration.getByLikes() != null) {
                    existingNotificationConfiguration.setByLikes(notificationConfiguration.getByLikes());
                }
                if (notificationConfiguration.getBySavings() != null) {
                    existingNotificationConfiguration.setBySavings(notificationConfiguration.getBySavings());
                }
                if (notificationConfiguration.getBySubscriptions() != null) {
                    existingNotificationConfiguration.setBySubscriptions(notificationConfiguration.getBySubscriptions());
                }
                if (notificationConfiguration.getByPrivacyRequests() != null) {
                    existingNotificationConfiguration.setByPrivacyRequests(notificationConfiguration.getByPrivacyRequests());
                }
                if (notificationConfiguration.getByPrivacyRequestsStatus() != null) {
                    existingNotificationConfiguration.setByPrivacyRequestsStatus(notificationConfiguration.getByPrivacyRequestsStatus());
                }

                return existingNotificationConfiguration;
            })
            .map(notificationConfigurationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationConfiguration.getId())
        );
    }

    /**
     * {@code GET  /notification-configurations} : get all the notificationConfigurations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificationConfigurations in body.
     */
    @GetMapping("/notification-configurations")
    public List<NotificationConfiguration> getAllNotificationConfigurations() {
        log.debug("REST request to get all NotificationConfigurations");
        return notificationConfigurationRepository.findAll();
    }

    /**
     * {@code GET  /notification-configurations/:id} : get the "id" notificationConfiguration.
     *
     * @param id the id of the notificationConfiguration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationConfiguration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notification-configurations/{id}")
    public ResponseEntity<NotificationConfiguration> getNotificationConfiguration(@PathVariable String id) {
        log.debug("REST request to get NotificationConfiguration : {}", id);
        Optional<NotificationConfiguration> notificationConfiguration = notificationConfigurationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(notificationConfiguration);
    }

    /**
     * {@code DELETE  /notification-configurations/:id} : delete the "id" notificationConfiguration.
     *
     * @param id the id of the notificationConfiguration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notification-configurations/{id}")
    public ResponseEntity<Void> deleteNotificationConfiguration(@PathVariable String id) {
        log.debug("REST request to delete NotificationConfiguration : {}", id);
        notificationConfigurationRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
