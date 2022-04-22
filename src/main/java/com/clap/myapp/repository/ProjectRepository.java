package com.clap.myapp.repository;

import com.clap.myapp.domain.Project;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the Project entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectRepository extends Neo4jRepository<Project, String> {}
