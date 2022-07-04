package com.clap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * A ContentCreator.
 */
@Node
@Getter
@Setter
public class ContentCreator extends User {

    @Property("full_name")
    private String fullName;

    @Property("country")
    private String country;

    @Property("city")
    private String city;

    @Relationship("HAS_PRIVACY_REQUEST")
    @JsonIgnoreProperties(value = { "company", "contentCreator" }, allowSetters = true)
    private Set<PrivacyRequest> privacyRequests = new HashSet<>();


    public Set<PrivacyRequest> getPrivacyRequests() {
        return this.privacyRequests;
    }

    public void setPrivacyRequests(Set<PrivacyRequest> privacyRequests) {
        if (this.privacyRequests != null) {
            this.privacyRequests.forEach(i -> i.setContentCreator(null));
        }
        if (privacyRequests != null) {
            privacyRequests.forEach(i -> i.setContentCreator(this));
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ContentCreator other = (ContentCreator) obj;
        if (city == null) {
            if (other.city != null)
                return false;
        } else if (!city.equals(other.city))
            return false;
        if (country == null) {
            if (other.country != null)
                return false;
        } else if (!country.equals(other.country))
            return false;
        if (fullName == null) {
            if (other.fullName != null)
                return false;
        } else if (!fullName.equals(other.fullName))
            return false;
        return true;
    }
}
