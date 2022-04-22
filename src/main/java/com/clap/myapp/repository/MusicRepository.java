package com.clap.myapp.repository;

import com.clap.myapp.domain.Music;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the Music entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MusicRepository extends Neo4jRepository<Music, String> {}
