package com.domgarr.RedditClone.service;

import com.domgarr.RedditClone.dto.PostRequest;
import com.domgarr.RedditClone.dto.PostResponse;
import com.domgarr.RedditClone.exception.SpringRedditException;
import com.domgarr.RedditClone.mapper.PostMapper;
import com.domgarr.RedditClone.model.Post;
import com.domgarr.RedditClone.model.Subreddit;
import com.domgarr.RedditClone.model.User;
import com.domgarr.RedditClone.repository.PostRepository;
import com.domgarr.RedditClone.repository.SubredditRepository;
import com.domgarr.RedditClone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(() ->
                new SpringRedditException("Subreddit of id: " + id + " not found."));
        return postRepository.findAllBySubreddit(subreddit).stream().map(postMapper :: mapPostToPostResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream().map(postMapper :: mapPostToPostResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new SpringRedditException("Post id not found."));
        return postMapper.mapPostToPostResponse(post);
    }

    public Post save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
            .orElseThrow( () -> new SpringRedditException("Subreddit not found."));

        User currentUser = authService.getCurrentUser();

        Post post = postMapper.mapPostRequestToPost(postRequest, subreddit, currentUser);


        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User currentUser = userRepository.findByUsername(username).orElseThrow(()->
            new SpringRedditException("Can't find username: " + username)
        );

        return postRepository.findAllByUser(currentUser).stream().map(postMapper::mapPostToPostResponse).collect(Collectors.toList());
    }
}