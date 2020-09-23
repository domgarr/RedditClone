package com.domgarr.RedditClone.repository;

import com.domgarr.RedditClone.model.Post;
import com.domgarr.RedditClone.model.User;
import com.domgarr.RedditClone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByIdDesc(Post post, User user);
}
