package com.clap.myapp.repository;

import com.clap.myapp.domain.Photography;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the Photography entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhotographyRepository extends Neo4jRepository<Photography, String> {}
