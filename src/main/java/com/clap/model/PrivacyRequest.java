package com.clap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

/**
 * A PrivacyRequest.
 */
@Node
@Getter
@Setter
public class PrivacyRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Property("request_id")
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("company_username")
    private String companyUsername;

    @Property("content_creator_username")
    private String contentCreatorUsername;

    @Property("request_state")
    private String requestState;

    @Property("request_date")
    private Date requestDate;

    @Relationship(value = "HAS_PRIVACY_REQUEST_COMPANY", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties(value = { "privacyRequests" }, allowSetters = true)
    private Company company;

    @Relationship(value = "HAS_PRIVACY_REQUEST_CONTENT_CREATOR", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties(value = { "privacyRequests" }, allowSetters = true)
    private ContentCreator contentCreator;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrivacyRequest)) {
            return false;
        }
        return id != null && id.equals(((PrivacyRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrivacyRequest{" +
            "id=" + getId() +
            ", requestState='" + getRequestState() + "'" +
            ", requestDate='" + getRequestDate() + "'" +
            "}";
    }
}
