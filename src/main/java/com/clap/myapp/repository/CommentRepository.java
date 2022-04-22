package com.clap.myapp.repository;

import com.clap.myapp.domain.Comment;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Neo4j repository for the Comment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentRepository extends Neo4jRepository<Comment, String> {}
