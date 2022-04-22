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
 * A ArtisticContent.
 */
@Node
public class ArtisticContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("title")
    private String title;

    @Property("description")
    private String description;

    @Property("content_url")
    private String contentUrl;

    @Property("upload_date")
    private LocalDate uploadDate;

    @Property("view_count")
    private Long viewCount;

    @Relationship(value = "HAS_ARTISTIC_CONTENT", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties(value = { "artisticContents" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    @Relationship(value = "HAS_ARTISTIC_CONTENT", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties(value = { "artisticContents" , "user" }, allowSetters = true)
    private Set<Project> projects = new HashSet<>();

    @Relationship(value = "HAS_ARTISTIC_CONTENT", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties(value = { "favourites","notifications","projects","followed","followers" }, allowSetters = true)
    private User owner;

    @Relationship(value = "HAS_ARTISTIC_CONTENT", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties(value = { "favourites","notifications","projects","followed","followers" }, allowSetters = true)
    private Set<User> users_favourites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ArtisticContent id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public ArtisticContent title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public ArtisticContent description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentUrl() {
        return this.contentUrl;
    }

    public ArtisticContent contentUrl(String contentUrl) {
        this.setContentUrl(contentUrl);
        return this;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public LocalDate getUploadDate() {
        return this.uploadDate;
    }

    public ArtisticContent uploadDate(LocalDate uploadDate) {
        this.setUploadDate(uploadDate);
        return this;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Long getViewCount() {
        return this.viewCount;
    }

    public ArtisticContent viewCount(Long viewCount) {
        this.setViewCount(viewCount);
        return this;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public ArtisticContent tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public ArtisticContent addTag(Tag tag) {
        this.tags.add(tag);
        tag.getArtisticContents().add(this);
        return this;
    }

    public ArtisticContent removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getArtisticContents().remove(this);
        return this;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public ArtisticContent projects(Set<Project> projects) {
        this.setProjects(projects);
        return this;
    }

    public ArtisticContent addProject(Project project) {
        this.projects.add(project);
        project.getArtisticContents().add(this);
        return this;
    }

    public ArtisticContent removeProject(Project project) {
        this.projects.remove(project);
        project.getArtisticContents().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtisticContent)) {
            return false;
        }
        return id != null && id.equals(((ArtisticContent) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArtisticContent{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", contentUrl='" + getContentUrl() + "'" +
            ", uploadDate='" + getUploadDate() + "'" +
            ", viewCount=" + getViewCount() +
            "}";
    }
}
