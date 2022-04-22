package com.clap.myapp.repository;

import com.clap.myapp.domain.ContentCreator;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the ContentCreator entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentCreatorRepository extends Neo4jRepository<ContentCreator, String> {}
