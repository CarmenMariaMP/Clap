package com.clap.myapp.repository;

import com.clap.myapp.domain.PrivacyRequest;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the PrivacyRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrivacyRequestRepository extends Neo4jRepository<PrivacyRequest, String> {}
