package com.clap.myapp.repository;

import com.clap.myapp.domain.Dance;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the Dance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DanceRepository extends Neo4jRepository<Dance, String> {}
