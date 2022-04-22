package com.clap.myapp.domain;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

/**
 * A Photography.
 */
@Node
public class Photography extends ArtisticContent {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("camera")
    private String camera;

    @Property("techniques")
    private String techniques;

    @Property("size")
    private String size;

    @Property("place")
    private String place;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Photography id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCamera() {
        return this.camera;
    }

    public Photography camera(String camera) {
        this.setCamera(camera);
        return this;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getTechniques() {
        return this.techniques;
    }

    public Photography techniques(String techniques) {
        this.setTechniques(techniques);
        return this;
    }

    public void setTechniques(String techniques) {
        this.techniques = techniques;
    }

    public String getSize() {
        return this.size;
    }

    public Photography size(String size) {
        this.setSize(size);
        return this;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPlace() {
        return this.place;
    }

    public Photography place(String place) {
        this.setPlace(place);
        return this;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Photography)) {
            return false;
        }
        return id != null && id.equals(((Photography) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
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
}
