package com.clap.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.Dance;

@Repository
public interface DanceRepository extends Neo4jRepository<Dance, String> {
    @Query ("MATCH (d:Dance{artistic_content_id:$artistic_content_id}) return d")
	Dance findDanceContentById(String artistic_content_id);
    
}
