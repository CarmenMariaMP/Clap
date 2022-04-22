package com.clap.myapp.domain;

import com.clap.myapp.domain.enumeration.DanceGenreType;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

/**
 * A Dance.
 */
@Node
public class Dance extends ArtisticContent {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("music")
    private String music;

    @Property("genres")
    private DanceGenreType genres;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Dance id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMusic() {
        return this.music;
    }

    public Dance music(String music) {
        this.setMusic(music);
        return this;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public DanceGenreType getGenres() {
        return this.genres;
    }

    public Dance genres(DanceGenreType genres) {
        this.setGenres(genres);
        return this;
    }

    public void setGenres(DanceGenreType genres) {
        this.genres = genres;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dance)) {
            return false;
        }
        return id != null && id.equals(((Dance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dance{" +
            "id=" + getId() +
            ", music='" + getMusic() + "'" +
            ", genres='" + getGenres() + "'" +
            "}";
    }
}
