package com.domgarr.RedditClone.mapper;

import com.domgarr.RedditClone.dto.PostRequest;
import com.domgarr.RedditClone.dto.PostResponse;
import com.domgarr.RedditClone.model.Post;
import com.domgarr.RedditClone.model.Subreddit;
import com.domgarr.RedditClone.model.User;
import com.domgarr.RedditClone.repository.CommentRepository;
import com.domgarr.RedditClone.repository.VoteRepository;
import com.domgarr.RedditClone.service.AuthService;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring") //Annotates class with Component.
public abstract class PostMapper {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    @Mapping(target="createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target="description", source = "postRequest.description")
    @Mapping(target="id", source = "postRequest.postId")
    @Mapping(target ="subreddit", source = "subreddit")
    @Mapping(target="user", source="user")
    @Mapping(target = "voteCount", constant = "0")
    public abstract Post mapPostRequestToPost(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target="id", source = "id")
    @Mapping(target="postName", source = "name")
    @Mapping(target="subredditName", source = "subreddit.name")
    @Mapping(target="username", source = "user.username")
    @Mapping(target="commentCount", expression = "java(commentCount(post))")
    @Mapping(target="duration", expression="java(getDuration(post))")
    public abstract PostResponse mapPostToPostResponse(Post post);

    Integer commentCount(Post post){
        return commentRepository.findAllByPost(post).size();
    }

    String getDuration(Post post){
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }




}
