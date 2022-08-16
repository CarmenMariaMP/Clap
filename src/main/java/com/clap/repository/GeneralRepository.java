package com.clap.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.General;

@Repository
public interface GeneralRepository extends Neo4jRepository<General, String> {
    @Query ("MATCH (g:General{artistic_content_id:$artistic_content_id}) return g")
	General findGeneralContentById(String artistic_content_id);
}
