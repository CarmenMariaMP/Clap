package com.clap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

/**
 * A Comment.
 */
@Node
@Getter
@Setter
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("text")
    private String text;

    @Property("date")
    private LocalDate date;

    @Relationship(value = "HAS_ROLE", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties(value = { "favourites","notifications","projects","followed","followers" }, allowSetters = true)
    private User user;

    @Relationship(value = "HAS_ROLE", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties(value = { "tags", "projects", "owner", "users_favourites" }, allowSetters = true)
    private ArtisticContent artisticContent;

    public Role type(String text) {
        this.setText(text);
        return this;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return id != null && id.equals(((Role) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Role{" +
            "id=" + getId() +
            ", type='" + getText() + "'" +
            "}";
    }
}
