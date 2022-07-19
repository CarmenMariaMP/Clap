package com.clap.model;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

/**
 * A Dance.
 */
@Node
@Getter
@Setter
public class Dance extends ArtisticContent {

    private static final long serialVersionUID = 1L;

    @Property("music")
    private String music;

    @Property("genres")
    private String genres;

    // prettier-ignore
    @Override
    public String toString() {
        return "Dance{" +
                "id=" + getId() +
                ", music='" + getMusic() + "'" +
                ", genres='" + getGenres() + "'" +
                "}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((genres == null) ? 0 : genres.hashCode());
        result = prime * result + ((music == null) ? 0 : music.hashCode());
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
        Dance other = (Dance) obj;
        if (genres != other.genres)
            return false;
        if (music == null) {
            if (other.music != null)
                return false;
        } else if (!music.equals(other.music))
            return false;
        return true;
    }
}
