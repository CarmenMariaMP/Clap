package com.clap.model;

import com.clap.model.enumeration.RequestStateType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
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

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("request_state")
    private RequestStateType requestState;

    @Property("request_date")
    private LocalDate requestDate;

    @Relationship(value = "HAS_PRIVACY_REQUEST", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties(value = { "privacyRequests" }, allowSetters = true)
    private Company company;

    @Relationship(value = "HAS_PRIVACY_REQUEST", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties(value = { "privacyRequests" }, allowSetters = true)
    private ContentCreator contentCreator;

    public PrivacyRequest requestState(RequestStateType requestState) {
        this.setRequestState(requestState);
        return this;
    }

    public PrivacyRequest requestDate(LocalDate requestDate) {
        this.setRequestDate(requestDate);
        return this;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public PrivacyRequest company(Company company) {
        this.setCompany(company);
        return this;
    }

    public PrivacyRequest contentCreatto(ContentCreator contentCreator) {
        this.setContentCreator(contentCreator);
        return this;
    }

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
