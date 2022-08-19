package com.clap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

/**
 * A Comment.
 */
@Node
@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = { "user","artisticContent"})
@NoArgsConstructor
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("text")
    private String text;

    @Property("date")
    private Date date;

    @Property("date_string")
    private String dateString;

    @Property("comment_responses")
    private List<CommentResponse> commentResponses;

    @Relationship(value = "HAS_COMMENT_USER", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties(value = { "privacyRequests" }, allowSetters = true)
    private User user;

    @Relationship(value = "HAS_COMMENT_ARTISTIC_CONTENT", direction = Relationship.Direction.OUTGOING)
    @JsonIgnoreProperties(value = { "privacyRequests" }, allowSetters = true)
    private ArtisticContent artisticContent;
}
