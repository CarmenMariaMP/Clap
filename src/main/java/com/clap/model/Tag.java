package com.clap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

/**
 * A Tag.
 */
@Node
@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = "artisticContents")
@NoArgsConstructor
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("text")
    private String text;

    @Relationship("HAS_TAG_ARTISTIC_CONTENT")
    @JsonIgnoreProperties(value = { "tags", "projects" }, allowSetters = true)
    private List<ArtisticContent> artisticContents = new ArrayList<ArtisticContent>();
}
