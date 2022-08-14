package com.clap.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.Tag;

@Repository
public interface TagRepository extends Neo4jRepository<Tag, String>{
    @Query("MATCH (t:Tag{text:$text}) return t")
    public Tag findTagByText(String text);

    @Query("MATCH (t:Tag)" +"  -[:HAS_TAG_ARTISTIC_CONTENT]" +" ->" +" (a:ArtisticContent{artistic_content_id:$artistic_content_id}) " +" return t")
    public List<Tag> findTagsByContentId(String artistic_content_id);
}
