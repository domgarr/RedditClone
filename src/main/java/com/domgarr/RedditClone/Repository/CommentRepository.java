package com.domgarr.RedditClone.Repository;

import com.domgarr.RedditClone.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
