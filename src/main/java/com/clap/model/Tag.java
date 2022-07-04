package com.clap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

/**
 * A Tag.
 */
@Node
@Getter
@Setter
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("name")
    private String name;

    @Relationship("HAS_ARTISTIC_CONTENT")
    @JsonIgnoreProperties(value = { "tags", "projects" }, allowSetters = true)
    private Set<ArtisticContent> artisticContents = new HashSet<>();

    public Tag name(String name) {
        this.setName(name);
        return this;
    }
    public void setArtisticContents(Set<ArtisticContent> artisticContents) {
        if (this.artisticContents != null) {
            this.artisticContents.forEach(i -> i.removeTag(this));
        }
        if (artisticContents != null) {
            artisticContents.forEach(i -> i.addTag(this));
        }
        this.artisticContents = artisticContents;
    }

    public Tag artisticContents(Set<ArtisticContent> artisticContents) {
        this.setArtisticContents(artisticContents);
        return this;
    }

    public Tag addArtisticContent(ArtisticContent artisticContent) {
        this.artisticContents.add(artisticContent);
        artisticContent.getTags().add(this);
        return this;
    }

    public Tag removeArtisticContent(ArtisticContent artisticContent) {
        this.artisticContents.remove(artisticContent);
        artisticContent.getTags().remove(this);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        return id != null && id.equals(((Tag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
