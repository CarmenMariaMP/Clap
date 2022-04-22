package com.clap.myapp.repository;

import com.clap.myapp.domain.ArtisticContent;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the ArtisticContent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtisticContentRepository extends Neo4jRepository<ArtisticContent, String> {}
