package com.clap.myapp.repository;

import com.clap.myapp.domain.PersistentToken;
import com.clap.myapp.domain.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 */
public interface PersistentTokenRepository extends Neo4jRepository<PersistentToken, String> {
    List<PersistentToken> findByUser(User user);

    List<PersistentToken> findByTokenDateBefore(LocalDate localDate);
}
