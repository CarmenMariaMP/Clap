package com.clap.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.Role;

@Repository
public interface RoleRepository extends Neo4jRepository<Role, String>{
    @Query("MATCH (r:Role)" +"  -[:HAS_ROLE_ARTISTIC_CONTENT]" +" ->" +" (a:ArtisticContent{artistic_content_id:$artistic_content_id}) " +" return r")
    public List<Role> findRolesByContentId(String artistic_content_id,String user_id);

    @Query("MATCH (r:Role)" +"-[:HAS_ROLE_USER] " +"->" +"(u:User{username:$username})" +" return r")
    public List<Role> findRolesByUsername(String username);
    
    
}
