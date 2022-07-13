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
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("text")
    private String text;

    @Property("date")
    private LocalDate date;

    @Relationship(value = "HAS_COMMENT", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties(value = { "privacyRequests" }, allowSetters = true)
    private User user;

    @Relationship(value = "HAS_COMMENT", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties(value = { "privacyRequests" }, allowSetters = true)
    private ArtisticContent artisticContent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        return id != null && id.equals(((Comment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comment{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
