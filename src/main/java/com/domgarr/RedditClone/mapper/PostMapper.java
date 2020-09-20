package com.domgarr.RedditClone.mapper;

import com.domgarr.RedditClone.dto.PostRequest;
import com.domgarr.RedditClone.dto.PostResponse;
import com.domgarr.RedditClone.model.Post;
import com.domgarr.RedditClone.model.Subreddit;
import com.domgarr.RedditClone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") //Annotates class with Component.
public interface PostMapper {

    @Mapping(target="createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target="description", source = "postRequest.description")
    @Mapping(target="id", source = "postRequest.postId")
    @Mapping(target ="subreddit", source = "subreddit")
    @Mapping(target="user", source="user")
    Post mapPostRequestToPost(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target="id", source = "id")
    @Mapping(target="postName", source = "name")
    @Mapping(target="subredditName", source = "subreddit.name")
    @Mapping(target="username", source = "user.username")
    PostResponse mapPostToPostResponse(Post post);


}
