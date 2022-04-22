package com.clap.myapp.repository;

import com.clap.myapp.domain.Notification;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends Neo4jRepository<Notification, String> {}
