package com.clap.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.Comment;

@Repository
public interface CommentRepository extends Neo4jRepository<Comment, String>{
    @Query("MATCH (c:Comment)" +"  -[:HAS_COMMENT_ARTISTIC_CONTENT]" +" ->" +" (a:ArtisticContent{artistic_content_id:$artistic_content_id}) " +" return c")
    public List<Comment> findCommentsByContentId(String artistic_content_id);

    @Query("MATCH (c:Comment{id:$id})" +"return c")
    public Comment findCommentById(String id);
}
