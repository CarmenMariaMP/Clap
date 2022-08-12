package com.clap.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.PaintingOrSculpture;

@Repository
public interface PaintingOrSculptureRepository extends Neo4jRepository<PaintingOrSculpture, String>{
    @Query ("MATCH (ps:PaintingOrSculpture{artistic_content_id:$artistic_content_id}) return ps")
	PaintingOrSculpture findPaintingOrSculptureContentById(String artistic_content_id);
}
