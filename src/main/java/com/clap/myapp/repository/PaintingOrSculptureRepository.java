package com.clap.myapp.repository;

import com.clap.myapp.domain.PaintingOrSculpture;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the PaintingOrSculpture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaintingOrSculptureRepository extends Neo4jRepository<PaintingOrSculpture, String> {}
