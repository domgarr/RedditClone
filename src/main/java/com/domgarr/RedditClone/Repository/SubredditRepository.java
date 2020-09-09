package com.domgarr.RedditClone.Repository;

import com.domgarr.RedditClone.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
}
