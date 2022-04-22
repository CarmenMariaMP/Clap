package com.clap.myapp.domain;

import com.clap.myapp.domain.enumeration.NotificationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

/**
 * A Notification.
 */
@Node
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("text")
    private String text;

    @Property("emision_date")
    private LocalDate emisionDate;

    @Property("read_date")
    private LocalDate readDate;

    @Property("type")
    private NotificationType type;

    @Relationship(value = "HAS_NOTIFICATION", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties(value = { "favourites","notifications","projects","followed","followers" }, allowSetters = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Notification id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public Notification text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getEmisionDate() {
        return this.emisionDate;
    }

    public Notification emisionDate(LocalDate emisionDate) {
        this.setEmisionDate(emisionDate);
        return this;
    }

    public void setEmisionDate(LocalDate emisionDate) {
        this.emisionDate = emisionDate;
    }

    public LocalDate getReadDate() {
        return this.readDate;
    }

    public Notification readDate(LocalDate readDate) {
        this.setReadDate(readDate);
        return this;
    }

    public void setReadDate(LocalDate readDate) {
        this.readDate = readDate;
    }

    public NotificationType getType() {
        return this.type;
    }

    public Notification type(NotificationType type) {
        this.setType(type);
        return this;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", emisionDate='" + getEmisionDate() + "'" +
            ", readDate='" + getReadDate() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
