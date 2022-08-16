package com.clap.model;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * A PaintingOrSculpture.
 */
@Node
@Data
@ToString()
@NoArgsConstructor
public class PaintingOrSculpture extends ArtisticContent {

    private static final long serialVersionUID = 1L;

    @Property("materials")
    private String materials;

    @Property("techniques")
    private String techniques;

    @Property("size")
    private String size;

    @Property("place")
    private String place;
}
