package com.domgarr.RedditClone.repository;

import com.domgarr.RedditClone.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.repositories
@Repository
public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
    Optional<Subreddit> findByName(String subredditName);
}
