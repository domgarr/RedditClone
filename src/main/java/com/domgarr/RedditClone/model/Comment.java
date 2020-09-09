package com.domgarr.RedditClone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Text is required.")
    private String text;

    @ManyToOne
    @JoinColumn(name="postId", referencedColumnName = "id")
    private Post post;
    private Instant instant;

    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName = "id")
    private User user;


}
