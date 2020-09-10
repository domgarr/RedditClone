package com.domgarr.RedditClone.repository;

import com.domgarr.RedditClone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
