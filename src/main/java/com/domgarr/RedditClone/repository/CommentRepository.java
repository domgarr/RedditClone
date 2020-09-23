package com.domgarr.RedditClone.repository;

import com.domgarr.RedditClone.dto.CommentRequest;
import com.domgarr.RedditClone.model.Comment;
import com.domgarr.RedditClone.model.Post;
import com.domgarr.RedditClone.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    //@EntityGraph(value="comment.user", type =  EntityGraph.EntityGraphType.FETCH)
    List<Comment> findAllByPost(Post post);

    List<Comment> findAllByUser(User user);
}
