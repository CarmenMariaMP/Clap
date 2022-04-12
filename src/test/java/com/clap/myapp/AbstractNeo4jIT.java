package com.clap.myapp;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.utility.DockerImageName;

public class AbstractNeo4jIT implements BeforeAllCallback {

    private static AtomicBoolean started = new AtomicBoolean(false);

    private static Neo4jContainer neo4jContainer = new Neo4jContainer(DockerImageName.parse("neo4j:4.4.4")).withoutAuthentication();

    public void beforeAll(ExtensionContext extensionContext) {
        if (!started.get()) {
            neo4jContainer.start();
            System.setProperty("spring.neo4j.uri", neo4jContainer.getBoltUrl());
            started.set(true);
        }
    }
}
