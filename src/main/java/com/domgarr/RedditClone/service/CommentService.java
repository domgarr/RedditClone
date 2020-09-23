package com.domgarr.RedditClone.service;

import com.domgarr.RedditClone.dto.CommentRequest;
import com.domgarr.RedditClone.exception.SpringRedditException;
import com.domgarr.RedditClone.mapper.CommentMapper;
import com.domgarr.RedditClone.model.Comment;
import com.domgarr.RedditClone.model.NotificationEmail;
import com.domgarr.RedditClone.model.Post;
import com.domgarr.RedditClone.model.User;
import com.domgarr.RedditClone.repository.CommentRepository;
import com.domgarr.RedditClone.repository.PostRepository;
import com.domgarr.RedditClone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentRequest commentRequest){
        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(()-> new SpringRedditException("Post not found with id: " + commentRequest.getPostId()));
        User user = authService.getCurrentUser();

        Comment comment = commentMapper.mapCommentRequestToComment(commentRequest, post, user);
        commentRepository.save(comment);

        String message = mailContentBuilder.build(user.getUsername() + " has posted on your post: " + "INSERT URL HERE");
        sendCommentNotication(message, user);
    }

    private void sendCommentNotication(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " commented on your post.", user.getEmail(), message));
    }


    public List<CommentRequest> getAllCommentsForPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()-> new SpringRedditException("Error"));
        post.setUser(authService.getCurrentUser());

        List<Comment> comments = commentRepository.findAllByPost(post);
        return comments.stream().map(commentMapper::mapCommentToCommentRequest)
                .collect(Collectors.toList());
    }

    public List<CommentRequest> getAllCommentsForUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new SpringRedditException("Username not found."));
        List<Comment> comments = commentRepository.findAllByUser(user);
        return comments.stream().map(commentMapper::mapCommentToCommentRequest).collect(Collectors.toList());
    }
}
