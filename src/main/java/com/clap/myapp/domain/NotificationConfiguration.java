package com.clap.myapp.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    public String getId() {
        return this.id;
    }

    public NotificationConfiguration id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getByComments() {
        return this.byComments;
    }

    public NotificationConfiguration byComments(Boolean byComments) {
        this.setByComments(byComments);
        return this;
    }

    public void setByComments(Boolean byComments) {
        this.byComments = byComments;
    }

    public Boolean getByLikes() {
        return this.byLikes;
    }

    public NotificationConfiguration byLikes(Boolean byLikes) {
        this.setByLikes(byLikes);
        return this;
    }

    public void setByLikes(Boolean byLikes) {
        this.byLikes = byLikes;
    }

    public Boolean getBySavings() {
        return this.bySavings;
    }

    public NotificationConfiguration bySavings(Boolean bySavings) {
        this.setBySavings(bySavings);
        return this;
    }

    public void setBySavings(Boolean bySavings) {
        this.bySavings = bySavings;
    }

    public Boolean getBySubscriptions() {
        return this.bySubscriptions;
    }

    public NotificationConfiguration bySubscriptions(Boolean bySubscriptions) {
        this.setBySubscriptions(bySubscriptions);
        return this;
    }

    public void setBySubscriptions(Boolean bySubscriptions) {
        this.bySubscriptions = bySubscriptions;
    }

    public Boolean getByPrivacyRequests() {
        return this.byPrivacyRequests;
    }

    public NotificationConfiguration byPrivacyRequests(Boolean byPrivacyRequests) {
        this.setByPrivacyRequests(byPrivacyRequests);
        return this;
    }

    public void setByPrivacyRequests(Boolean byPrivacyRequests) {
        this.byPrivacyRequests = byPrivacyRequests;
    }

    public Boolean getByPrivacyRequestsStatus() {
        return this.byPrivacyRequestsStatus;
    }

    public NotificationConfiguration byPrivacyRequestsStatus(Boolean byPrivacyRequestsStatus) {
        this.setByPrivacyRequestsStatus(byPrivacyRequestsStatus);
        return this;
    }

    public void setByPrivacyRequestsStatus(Boolean byPrivacyRequestsStatus) {
        this.byPrivacyRequestsStatus = byPrivacyRequestsStatus;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public NotificationConfiguration user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
