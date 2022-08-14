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

    @Query("MATCH (r:Role{role_id:$role_id})" + "-[:HAS_ROLE_ARTISTIC_CONTENT] " + "->" + "(a:ArtisticContent) " + " return a")
    ArtisticContent findByRoleId(String role_id);

    @Query("MATCH (c:Comment{id:$comment_id})" + "-[:HAS_COMMENT_ARTISTIC_CONTENT] " + "->" + "(a:ArtisticContent) " + " return a")
    ArtisticContent findByCommentId(String comment_id);

    @Query("MATCH(ac:ArtisticContent{artistic_content_id:$artistic_content_id})" + " return ac.content_url")
    String findContentUrlById(String artistic_content_id);
}
