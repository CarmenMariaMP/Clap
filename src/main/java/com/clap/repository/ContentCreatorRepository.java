package com.clap.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.ContentCreator;

@Repository
public interface ContentCreatorRepository extends Neo4jRepository<ContentCreator, String> {
    @Query("MATCH (n:ContentCreator{username: $username} ) " + "return n")
	ContentCreator getContentCreatorByUsername(String username);

    @Query("MATCH (n:ContentCreator{user_id: $user_id} ) " + "return n")
	ContentCreator getContentCreatorById(String user_id);
}
