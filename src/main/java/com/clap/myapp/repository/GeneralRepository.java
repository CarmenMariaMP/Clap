package com.clap.myapp.repository;

import com.clap.myapp.domain.General;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the General entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeneralRepository extends Neo4jRepository<General, String> {}
