package com.clap.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.clap.model.Subscription;
import com.clap.model.User;

@Repository
public interface SubscriptionRepository extends Neo4jRepository<Subscription, String>{
    public Set<Subscription> findByFollower(User follower);
    public Set<Subscription> findByFollowed(User followed);

    @Query("MATCH (s:Subscription)" +" -[:HAS_FOLLOWED]" +"->" +"(u:User{username: $username_followed}) " +"WITH s" +" MATCH (s:Subscription)" +" -[:HAS_FOLLOWER]" +"->" +"(u:User{username: $username_follower})" +"return s")
    public Optional<Subscription> findByFollowedAndFollower(String username_followed, String username_follower);

    @Query("MATCH (s:Subscription)" +" -[:HAS_FOLLOWED]" +"->" +"(u:User{username: $username})" +"return count(s) as count")
    public Integer followersCount(String username);

    @Query("MATCH (s:Subscription)" +" -[:HAS_FOLLOWER]" +"->" +"(u:User{username: $username})" +"return count(s) as count")
    public Integer followedsCount(String username);

    @Query("MATCH (s:Subscription)" +"-[:HAS_FOLLOWED]" +"->" +"(u:User{username:$username})" +"detach delete s")
    public void deleteSubscriptionsByFollowedRelation(String username);

    @Query("MATCH (s:Subscription)" +"-[:HAS_FOLLOWER]" +"->" +"(u:User{username:$username})" +"detach delete s")
    public void deleteSubscriptionsByFollowerRelation(String username);
}
