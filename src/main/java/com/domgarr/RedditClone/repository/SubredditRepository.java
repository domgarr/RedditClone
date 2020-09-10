package com.domgarr.RedditClone.repository;

import com.domgarr.RedditClone.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
}
