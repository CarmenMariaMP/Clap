package com.clap.repository;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.Favourite;

@Repository
public interface FavouriteRepository extends Neo4jRepository<Favourite, String>{
    @Query("MATCH (f:Favourite)" +" -[:HAS_FAVOURITE_USER]" +"->" +"(u:User{user_id: $user_id})" +" WITH f" +" MATCH (f:Favourite) " +"-[:HAS_FAVOURITE_ARTISTIC_CONTENT]" +"->" +"(a:ArtisticContent{artistic_content_id: $artistic_content_id})" +" return f")
    public Optional<Favourite> findByUserAndContent(String artistic_content_id, String user_id);

    @Query("MATCH (f:Favourite)" +" -[:HAS_FAVOURITE_USER]" +"->" +"(u:User{user_id: $user_id})" +"return f")
    public Optional<Favourite> findByUserId(String user_id);

    @Query("MATCH (f:Favourite)-[r:HAS_FAVOURITE_ARTISTIC_CONTENT]->(a:ArtisticContent{artistic_content_id:$artistic_content_id})DELETE r")
    public void deleteArtisticContentRelationship(String artistic_content_id);
    
}
