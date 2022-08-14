package com.clap.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.CommentResponse;

@Repository
public interface CommentResponseRepository extends Neo4jRepository<CommentResponse, String> {
    @Query("MATCH (cr:CommentResponse)" + "-[:BELONGS_TO_COMMENT]" + "->" + "(c:Comment{id:$id}) return cr")
    public List<CommentResponse> findCommentResponsesByCommentId(String id);
}
