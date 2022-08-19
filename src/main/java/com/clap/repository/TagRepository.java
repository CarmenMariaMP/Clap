package com.clap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.Tag;

@Repository
public interface TagRepository extends Neo4jRepository<Tag, String>{
    @Query("MATCH (t:Tag)" +"  -[:HAS_TAG_ARTISTIC_CONTENT]" +" ->" +" (a:ArtisticContent{artistic_content_id:$artistic_content_id}) " +" return t")
    public List<Tag> findTagsByContentId(String artistic_content_id);

    @Query("MATCH (t:Tag{text:$text}) return t")
    public Optional<Tag> findTagByText(String text);

    @Query("MATCH (t:Tag{text:$text})-[r:HAS_TAG_ARTISTIC_CONTENT]->(a:ArtisticContent{artistic_content_id:$artistic_content_id}) DELETE r")
    public void deleteArtisticContentRelationship(String text,String artistic_content_id);

    @Query("MATCH (t:Tag{tag_id:$tag_id}) return t")
    public Optional<Tag> findById(String tag_id);

    @Query("MATCH (t:Tag) return t")
    List<Tag> findAllTags();
}
