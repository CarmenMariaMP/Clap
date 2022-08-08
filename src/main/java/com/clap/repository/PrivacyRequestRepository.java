package com.clap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import com.clap.model.PrivacyRequest;

public interface PrivacyRequestRepository extends Neo4jRepository<PrivacyRequest, String>{
    @Query("MATCH (p:PrivacyRequest)" +" -[:HAS_PRIVACY_REQUEST_CONTENT_CREATOR]" +" ->" +" (u:User{username: $username_content_creator}) " +" WITH p" +"  MATCH (p:PrivacyRequest)" +" -[:HAS_PRIVACY_REQUEST_COMPANY]" +" ->" +" (u:User{username: $username_company})" +"  return p")
    public Optional<PrivacyRequest> findByCreatorAndCompany(String username_content_creator, String username_company);

    @Query("MATCH (p:PrivacyRequest{request_id:$request_id})" +" return p")
    public Optional<PrivacyRequest>  findById(String request_id);

    @Query("MATCH (p:PrivacyRequest)" +"-[:HAS_PRIVACY_REQUEST_CONTENT_CREATOR]" +"-> " +"(c:ContentCreator{username: $username}) return p")
    public List<PrivacyRequest> findPrivacyRequestsByContentCreatorUsernme(String username);

    @Query("MATCH (p:PrivacyRequest)" +"-[:HAS_PRIVACY_REQUEST_COMPANY]" +"-> " +"(c:Company{username: $username}) return p")
    public List<PrivacyRequest> findPrivacyRequestsByCompany(String username);

    @Query("MATCH(p:PrivacyRequest{content_creator_username:$username,request_state:$request_state})" +" return count(p) as count")
    public Integer findNumberPendingRequestsByContentCreator(String username,String request_state);
}