package com.clap.myapp.domain;

import com.clap.myapp.domain.enumeration.CinemaGenreType;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

/**
 * A Cinema.
 */
@Node
public class Cinema extends ArtisticContent {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("genres")
    private CinemaGenreType genres;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Cinema id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CinemaGenreType getGenres() {
        return this.genres;
    }

    public Cinema genres(CinemaGenreType genres) {
        this.setGenres(genres);
        return this;
    }

    public void setGenres(CinemaGenreType genres) {
        this.genres = genres;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cinema)) {
            return false;
        }
        return id != null && id.equals(((Cinema) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cinema{" +
            "id=" + getId() +
            ", genres='" + getGenres() + "'" +
            "}";
    }
}
