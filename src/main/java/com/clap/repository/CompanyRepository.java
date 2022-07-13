package com.clap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import com.clap.model.Company;

public interface CompanyRepository extends Neo4jRepository<Company, String>{
    @Query("MATCH (co:Company) " + "RETURN DISTINCT co.username")
	List<String> getCompanies();

    public Optional<Company> findByUsername(String username);
    
}
