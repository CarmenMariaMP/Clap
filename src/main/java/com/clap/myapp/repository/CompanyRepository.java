package com.clap.myapp.repository;

import com.clap.myapp.domain.Company;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the Company entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyRepository extends Neo4jRepository<Company, String> {}
