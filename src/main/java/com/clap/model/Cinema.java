package com.clap.model;

import com.clap.model.enumeration.CinemaGenreType;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

/**
 * A Cinema.
 */
@Node
@Getter
@Setter
public class Cinema extends ArtisticContent {

    private static final long serialVersionUID = 1L;

    @Property("genres")
    private CinemaGenreType genres;

    public Cinema genres(CinemaGenreType genres) {
        this.setGenres(genres);
        return this;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cinema{" +
                "id=" + getId() +
                ", genres='" + getGenres() + "'" +
                "}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((genres == null) ? 0 : genres.hashCode());
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
        Cinema other = (Cinema) obj;
        if (genres != other.genres)
            return false;
        return true;
    }
}
