package com.clap.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import com.clap.model.Company;

public interface CompanyRepository extends Neo4jRepository<Company, String>{
    @Query("MATCH (n:Company{username: $username} ) " + "return n")
	Company getCompanyByUsername(String username);

    @Query("MATCH (n:Company{user_id: $user_id} ) " + "return n")
	Company getCompanyById(String user_id);
    
}
