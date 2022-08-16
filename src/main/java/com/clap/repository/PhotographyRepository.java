package com.clap.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.Photography;

@Repository
public interface PhotographyRepository  extends Neo4jRepository<Photography, String> {
    @Query ("MATCH (p:Photography{artistic_content_id:$artistic_content_id}) return p")
	Photography findPhotographyContentById(String artistic_content_id);
}
