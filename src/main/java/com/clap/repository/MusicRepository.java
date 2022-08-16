package com.clap.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.Music;

@Repository
public interface MusicRepository extends Neo4jRepository<Music, String> {
    @Query ("MATCH (m:Music{artistic_content_id:$artistic_content_id}) return m")
	Music findMusicContentById(String artistic_content_id);
    
}
