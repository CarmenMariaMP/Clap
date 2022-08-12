package com.clap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.ArtisticContent;

@Repository
public interface ArtisticContentRepository extends Neo4jRepository<ArtisticContent, String> {
    public Optional<ArtisticContent> findById(String id);

    @Query("MATCH (ac:ArtisticContent) " + "RETURN DISTINCT ac")
	List<ArtisticContent> getArtisticContent();

    @Query("MATCH (u:User{username: $username })" + " -[:HAS_ARTISTIC_CONTENT_OWNER]" +"->" +"(ac:ArtisticContent)return ac")
	List<ArtisticContent> findByOwner(String username);

    @Query ("MATCH (a:ArtisticContent{title:$title})" + " return a.artistic_content_id")
    public String findIdByTitle(String title);

    @Query ("MATCH (a:ArtisticContent{artistic_content_id:$artistic_content_id}) " + "return a.type")
    String getTypeById(String artistic_content_id);
}
