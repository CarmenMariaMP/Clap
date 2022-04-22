package com.clap.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

/**
 * A ContentCreator.
 */
@Node
public class ContentCreator extends User {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("full_name")
    private String fullName;

    @Property("country")
    private String country;

    @Property("city")
    private String city;

    @Relationship("HAS_PRIVACY_REQUEST")
    @JsonIgnoreProperties(value = { "company", "contentCreator" }, allowSetters = true)
    private Set<PrivacyRequest> privacyRequests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ContentCreator id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public ContentCreator fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountry() {
        return this.country;
    }

    public ContentCreator country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return this.city;
    }

    public ContentCreator city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<PrivacyRequest> getPrivacyRequests() {
        return this.privacyRequests;
    }

    public void setPrivacyRequests(Set<PrivacyRequest> privacyRequests) {
        if (this.privacyRequests != null) {
            this.privacyRequests.forEach(i -> i.setContentCreatto(null));
        }
        if (privacyRequests != null) {
            privacyRequests.forEach(i -> i.setContentCreatto(this));
        }
        this.privacyRequests = privacyRequests;
    }

    public ContentCreator privacyRequests(Set<PrivacyRequest> privacyRequests) {
        this.setPrivacyRequests(privacyRequests);
        return this;
    }

    public ContentCreator addPrivacyRequest(PrivacyRequest privacyRequest) {
        this.privacyRequests.add(privacyRequest);
        return this;
    }

    public ContentCreator removePrivacyRequest(PrivacyRequest privacyRequest) {
        this.privacyRequests.remove(privacyRequest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContentCreator)) {
            return false;
        }
        return id != null && id.equals(((ContentCreator) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContentCreator{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", country='" + getCountry() + "'" +
            ", city='" + getCity() + "'" +
            "}";
    }
}
