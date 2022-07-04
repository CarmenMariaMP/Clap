package com.clap.model;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import lombok.Getter;
import lombok.Setter;

/**
 * A Photography.
 */
@Node
@Getter
@Setter
public class Photography extends ArtisticContent {

    private static final long serialVersionUID = 1L;

    @Property("camera")
    private String camera;

    @Property("techniques")
    private String techniques;

    @Property("size")
    private String size;

    @Property("place")
    private String place;

    public String getCamera() {
        return this.camera;
    }

    public Photography camera(String camera) {
        this.setCamera(camera);
        return this;
    }

    public Photography techniques(String techniques) {
        this.setTechniques(techniques);
        return this;
    }

    public Photography size(String size) {
        this.setSize(size);
        return this;
    }
    public Photography place(String place) {
        this.setPlace(place);
        return this;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Photography{" +
                "id=" + getId() +
                ", camera='" + getCamera() + "'" +
                ", techniques='" + getTechniques() + "'" +
                ", size='" + getSize() + "'" +
                ", place='" + getPlace() + "'" +
                "}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((camera == null) ? 0 : camera.hashCode());
        result = prime * result + ((place == null) ? 0 : place.hashCode());
        result = prime * result + ((size == null) ? 0 : size.hashCode());
        result = prime * result + ((techniques == null) ? 0 : techniques.hashCode());
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
        Photography other = (Photography) obj;
        if (camera == null) {
            if (other.camera != null)
                return false;
        } else if (!camera.equals(other.camera))
            return false;
        if (place == null) {
            if (other.place != null)
                return false;
        } else if (!place.equals(other.place))
            return false;
        if (size == null) {
            if (other.size != null)
                return false;
        } else if (!size.equals(other.size))
            return false;
        if (techniques == null) {
            if (other.techniques != null)
                return false;
        } else if (!techniques.equals(other.techniques))
            return false;
        return true;
    }
}
