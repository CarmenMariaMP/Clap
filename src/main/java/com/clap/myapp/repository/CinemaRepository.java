package com.clap.myapp.repository;

import com.clap.myapp.domain.Cinema;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the Cinema entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CinemaRepository extends Neo4jRepository<Cinema, String> {}
