package com.clap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.User;

@Repository
public interface UserRepository extends Neo4jRepository<User, String> {
    @Query("MATCH (u:User) " + "RETURN u.user_id")
	List<String> findAllUsersId();

    public Optional<User> findByUsername(String username);

    @Query("MATCH (u:User)" +" -[:HAS_ARTISTIC_CONTENT_OWNER]" +"->" + "(ac:ArtisticContent{title: $title})" +"return u")
    User getOwnerByTitle(String title);

    @Query ("MATCH (n:User{username:$username}) return n.type")
    String getTypeByUsername(String username);

    @Query ("MATCH (n:User{user_id:$user_id}) return n.type")
    String getTypeById(String user_id);

    @Query("MATCH (u:User)" +"  return u.username")
    List<String> getAllUsernames();

    @Query("MATCH (u:User)" +"-[:HAS_ARTISTIC_CONTENT_OWNER]" +"->" +"(ac:ArtisticContent{artistic_content_id: $artistic_content_id}) " +"return u")
    User findUserByArtisticContentId(String artistic_content_id);

    @Query("MATCH (c:Comment{id:$id})" +"-[:HAS_COMMENT_USER]" +" -> " +"(u:User)" +"  return u")
    User findUserByCommentId(String id);

    @Query("MATCH (c:Comment{id:$id})" +"-[:HAS_COMMENT_RESPONSE_USER]" +" -> " +"(u:User)" +"  return u")
    User findUserByCommentResponseId(String id);

    @Query("MATCH (u:User{username:$username}) detach delete u")
    public void deleteUser(String username);
}
