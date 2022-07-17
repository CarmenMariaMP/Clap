package com.clap.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.clap.model.ArtisticContent;

public interface ArtisticContentRepository extends Neo4jRepository<ArtisticContent, String> {
    @Query("MATCH (ac:ArtisticContent) " + "RETURN DISTINCT ac")
	List<ArtisticContent> getArtisticContent();

    public Optional<ArtisticContent> findContentByTitle(String title);

    ArtisticContent findByTitle(@Param("title") String title);   
    Collection<ArtisticContent> findByTitleLike(@Param("title") String title);
    
}
