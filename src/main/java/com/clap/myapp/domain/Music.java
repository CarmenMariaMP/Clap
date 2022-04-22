package com.clap.myapp.domain;

import com.clap.myapp.domain.enumeration.MusicGenreType;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

/**
 * A Music.
 */
@Node
public class Music extends ArtisticContent {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("genres")
    private MusicGenreType genres;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Music id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MusicGenreType getGenres() {
        return this.genres;
    }

    public Music genres(MusicGenreType genres) {
        this.setGenres(genres);
        return this;
    }

    public void setGenres(MusicGenreType genres) {
        this.genres = genres;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Music)) {
            return false;
        }
        return id != null && id.equals(((Music) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Music{" +
            "id=" + getId() +
            ", genres='" + getGenres() + "'" +
            "}";
    }
}
