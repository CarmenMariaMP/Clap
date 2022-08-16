package com.clap.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

/**
 * A Music.
 */
@Node
@Data
@ToString()
@NoArgsConstructor
public class Music extends ArtisticContent {

    private static final long serialVersionUID = 1L;

    @Property("genres")
    private List<String> genres;

}
