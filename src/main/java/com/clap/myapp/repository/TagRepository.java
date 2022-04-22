package com.clap.myapp.repository;

import com.clap.myapp.domain.Tag;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the Tag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagRepository extends Neo4jRepository<Tag, String> {}
