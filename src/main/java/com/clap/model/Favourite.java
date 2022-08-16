package com.clap.model;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Node
@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = {})
@NoArgsConstructor
public class Favourite {
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    @Property("favourite_id")
    private String id;

    @Relationship(value = "HAS_FAVOURITE_USER", direction = Relationship.Direction.OUTGOING)
    User user;

    @Relationship(value = "HAS_FAVOURITE_ARTISTIC_CONTENT", direction = Relationship.Direction.OUTGOING)
    List<ArtisticContent> artisticContent;
}
