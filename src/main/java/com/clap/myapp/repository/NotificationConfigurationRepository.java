package com.clap.myapp.repository;

import com.clap.myapp.domain.NotificationConfiguration;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the NotificationConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationConfigurationRepository extends Neo4jRepository<NotificationConfiguration, String> {}
