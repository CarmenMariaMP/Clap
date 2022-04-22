package com.clap.myapp.domain;

import com.clap.myapp.domain.enumeration.RequestStateType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public PrivacyRequest id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RequestStateType getRequestState() {
        return this.requestState;
    }

    public PrivacyRequest requestState(RequestStateType requestState) {
        this.setRequestState(requestState);
        return this;
    }

    public void setRequestState(RequestStateType requestState) {
        this.requestState = requestState;
    }

    public LocalDate getRequestDate() {
        return this.requestDate;
    }

    public PrivacyRequest requestDate(LocalDate requestDate) {
        this.setRequestDate(requestDate);
        return this;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public PrivacyRequest company(Company company) {
        this.setCompany(company);
        return this;
    }

    public ContentCreator getContentCreatto() {
        return this.contentCreator;
    }

    public void setContentCreatto(ContentCreator contentCreator) {
        this.contentCreator = contentCreator;
    }

    public PrivacyRequest contentCreatto(ContentCreator contentCreator) {
        this.setContentCreatto(contentCreator);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
