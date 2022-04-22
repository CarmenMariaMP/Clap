package com.clap.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

/**
 * A Project.
 */
@Node
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("creation_date")
    private LocalDate creationDate;

    @Relationship("HAS_ARTISTIC_CONTENT")
    @JsonIgnoreProperties(value = { "tags", "projects", "owner", "users_favourites" }, allowSetters = true)
    private Set<ArtisticContent> artisticContents = new HashSet<>();

    @Relationship(value = "HAS_PROJECT", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties(value = { "favourites","notifications","projects","followed","followers" }, allowSetters = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Project id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public Project creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Set<ArtisticContent> getArtisticContents() {
        return this.artisticContents;
    }

    public void setArtisticContents(Set<ArtisticContent> artisticContents) {
        if (this.artisticContents != null) {
            this.artisticContents.forEach(i -> i.removeProject(this));
        }
        if (artisticContents != null) {
            artisticContents.forEach(i -> i.addProject(this));
        }
        this.artisticContents = artisticContents;
    }

    public Project artisticContents(Set<ArtisticContent> artisticContents) {
        this.setArtisticContents(artisticContents);
        return this;
    }

    public Project addArtisticContent(ArtisticContent artisticContent) {
        this.artisticContents.add(artisticContent);
        artisticContent.getProjects().add(this);
        return this;
    }

    public Project removeArtisticContent(ArtisticContent artisticContent) {
        this.artisticContents.remove(artisticContent);
        artisticContent.getProjects().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return id != null && id.equals(((Project) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
