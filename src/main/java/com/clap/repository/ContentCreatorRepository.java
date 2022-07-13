package com.clap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.ContentCreator;

@Repository
public interface ContentCreatorRepository extends Neo4jRepository<ContentCreator, String> {
    @Query("MATCH (cr:ContentCreator) " + "RETURN DISTINCT cr.username")
	List<String> getContentCreators();

    public Optional<ContentCreator> findByUsername(String username);

}
