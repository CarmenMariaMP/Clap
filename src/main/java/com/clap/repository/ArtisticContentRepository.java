package com.clap.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import com.clap.model.ArtisticContent;

public interface ArtisticContentRepository extends Neo4jRepository<ArtisticContent, String> {
    @Query("MATCH (ac:ArtisticContent) " + "RETURN DISTINCT ac.title")
	List<String> getArtisticContent();
    
}
