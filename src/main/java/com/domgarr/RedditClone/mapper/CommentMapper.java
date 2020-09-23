package com.domgarr.RedditClone.mapper;

import com.domgarr.RedditClone.dto.CommentRequest;
import com.domgarr.RedditClone.model.Comment;
import com.domgarr.RedditClone.model.Post;
import com.domgarr.RedditClone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target="text", source="commentRequest.text")
    @Mapping(target="createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target="post", source = "post")
    @Mapping(target="user", source = "user")
    Comment mapCommentRequestToComment(CommentRequest commentRequest, Post post, User user);

    @Mapping(target="postId", expression = "java(comment.getPost().getId())")
    @Mapping(target="username",expression= "java(comment.getUser().getUsername())")
    CommentRequest mapCommentToCommentRequest(Comment comment);

}
