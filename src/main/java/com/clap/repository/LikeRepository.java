package com.clap.repository;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.Like;

@Repository
public interface LikeRepository extends Neo4jRepository<Like, String>{
    @Query("MATCH (l:Like)" +" -[:HAS_LIKE_USER]" +"->" +"(u:User{user_id:$user_id})" +" WITH collect(l) as out" +" MATCH (l:Like) " +"-[:HAS_LIKE_ARTISTIC_CONTENT]" +"->" +"(a:ArtisticContent{artistic_content_id: $artistic_content_id})" +"return l")
    public Optional<Like> findByUserAndContent(String user_id, String artistic_content_id);

    @Query("MATCH (l:Like)" +" -[:HAS_LIKE_USER]" +"->" +"(u:User{user_id: $user_id})" +"return l")
    public Optional<Like> findByUserId(String user_id);

    @Query("MATCH (l:Like)-[r:HAS_LIKE_ARTISTIC_CONTENT]->(a:ArtisticContent{artistic_content_id:$artistic_content_id})DELETE r")
    public void deleteArtisticContentRelationship(String artistic_content_id);

    @Query("MATCH (l:Like)-[r:HAS_LIKE_ARTISTIC_CONTENT]->(a:ArtisticContent{artistic_content_id:$artistic_content_id}) return count(r)")
    Integer likeCount(String artistic_content_id);
    
}
