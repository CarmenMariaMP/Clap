package com.clap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

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
@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = { "tags","owner","users_favourites"})
@NoArgsConstructor
public abstract class ArtisticContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Property("artistic_content_id")
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("type")
    @NotNull
	String type;

    @Property("title")
    private String title;

    @Property("description")
    private String description;

    @Property("content_url")
    private String contentUrl;

    @Property("upload_date")
    private Date uploadDate;

    @Property("viewCount")
    private Integer viewCount;

    @Relationship(value = "HAS_ARTISTIC_CONTENT_TAG", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties(value = { "artisticContents" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    @Relationship(value = "HAS_ARTISTIC_CONTENT_OWNER", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties(value = { "favourites"}, allowSetters = true)
    private User owner;

    @Relationship(value = "HAS_ARTISTIC_CONTENT_FAVOURITES_USERS", direction = Relationship.Direction.INCOMING)
    @JsonIgnoreProperties(value = { "favourites"}, allowSetters = true)
    private Set<User> users_favourites = new HashSet<>();

    public ArtisticContent title(String title) {
        this.setTitle(title);
        return this;
    }
}
