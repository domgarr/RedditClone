package com.domgarr.RedditClone.controller;

import com.domgarr.RedditClone.dto.CommentRequest;
import com.domgarr.RedditClone.dto.CommentResponse;
import com.domgarr.RedditClone.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentRequest commentRequest){
        commentService.save(commentRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<List<CommentRequest>> getAllCommentsForPost(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsForPost(id));

    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<CommentRequest>> getAllCommentsForUsername(@PathVariable String username){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsForUsername(username));
    }
}
