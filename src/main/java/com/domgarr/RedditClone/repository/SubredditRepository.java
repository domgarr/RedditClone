package com.domgarr.RedditClone.repository;

import com.domgarr.RedditClone.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.repositories
public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
    Optional<Subreddit> findByName(String subredditName);
}
