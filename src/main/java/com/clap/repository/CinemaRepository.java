package com.clap.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.Cinema;

@Repository
public interface CinemaRepository extends Neo4jRepository<Cinema, String> {
    @Query ("MATCH (c:Cinema{artistic_content_id:$artistic_content_id}) return c")
	Cinema findCinemaContentById(String artistic_content_id);
    
}
