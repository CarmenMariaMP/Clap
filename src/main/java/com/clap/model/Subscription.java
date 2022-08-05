package com.clap.model;

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
public class Subscription {
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    @Property("subscription_id")
    private String id;

    @Relationship(value = "HAS_FOLLOWER", direction = Relationship.Direction.OUTGOING)
    User follower;

    @Relationship(value = "HAS_FOLLOWED", direction = Relationship.Direction.OUTGOING)
    User followed;
}
