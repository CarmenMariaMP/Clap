package com.clap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.User;

@Repository
public interface UserRepository extends Neo4jRepository<User, String> {
    @Query("MATCH (u:User) " + "RETURN DISTINCT u.username")
	List<String> getUsers();

    public Optional<User> findByUsername(String username);

    @Query("MATCH (u:User)" +" -[:HAS_ARTISTIC_CONTENT_OWNER]" +"->" + "(ac:ArtisticContent{title: $title})" +"return u")
    User getOwnerByTitle(String title);
}
