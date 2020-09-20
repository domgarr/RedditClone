package com.domgarr.RedditClone.repository;

import com.domgarr.RedditClone.model.Post;
import com.domgarr.RedditClone.model.Subreddit;
import com.domgarr.RedditClone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    //find - *All* - By , the All is just for readability, all that matters is pre (find) and post(By)
    List<Post> findAllBySubreddit(Subreddit subreddit);
    List<Post> findAllByUser(User user);
}
