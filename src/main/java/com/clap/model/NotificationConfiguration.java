package com.clap.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

/**
 * A NotificationConfiguration.
 */
@Node
@Getter
@Setter
public class NotificationConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("by_comments")
    private Boolean byComments;

    @Property("by_likes")
    private Boolean byLikes;

    @Property("by_savings")
    private Boolean bySavings;

    @Property("by_subscriptions")
    private Boolean bySubscriptions;

    @Property("by_privacy_requests")
    private Boolean byPrivacyRequests;

    @Property("by_privacy_requests_status")
    private Boolean byPrivacyRequestsStatus;

    @Relationship(value = "HAS_NOTIFICATION_CONFIGURATION", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties(value = { "favourites","notifications","projects","followed","followers" }, allowSetters = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    
    public NotificationConfiguration byComments(Boolean byComments) {
        this.setByComments(byComments);
        return this;
    }

    public NotificationConfiguration byLikes(Boolean byLikes) {
        this.setByLikes(byLikes);
        return this;
    }

    public NotificationConfiguration bySavings(Boolean bySavings) {
        this.setBySavings(bySavings);
        return this;
    }

    public void setBySavings(Boolean bySavings) {
        this.bySavings = bySavings;
    }

    public NotificationConfiguration bySubscriptions(Boolean bySubscriptions) {
        this.setBySubscriptions(bySubscriptions);
        return this;
    }

    public NotificationConfiguration byPrivacyRequests(Boolean byPrivacyRequests) {
        this.setByPrivacyRequests(byPrivacyRequests);
        return this;
    }
    public NotificationConfiguration byPrivacyRequestsStatus(Boolean byPrivacyRequestsStatus) {
        this.setByPrivacyRequestsStatus(byPrivacyRequestsStatus);
        return this;
    }

    public NotificationConfiguration user(User user) {
        this.setUser(user);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationConfiguration)) {
            return false;
        }
        return id != null && id.equals(((NotificationConfiguration) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationConfiguration{" +
            "id=" + getId() +
            ", byComments='" + getByComments() + "'" +
            ", byLikes='" + getByLikes() + "'" +
            ", bySavings='" + getBySavings() + "'" +
            ", bySubscriptions='" + getBySubscriptions() + "'" +
            ", byPrivacyRequests='" + getByPrivacyRequests() + "'" +
            ", byPrivacyRequestsStatus='" + getByPrivacyRequestsStatus() + "'" +
            "}";
    }
}
