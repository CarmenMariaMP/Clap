package com.clap.model;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

/**
 * A ContentCreator.
 */
@Node
@Getter
@Setter
public class ContentCreator extends User {

    @Property("fullName")
    private String fullName;

    @Property("country")
    private String country;

    @Property("city")
    private String city;

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
