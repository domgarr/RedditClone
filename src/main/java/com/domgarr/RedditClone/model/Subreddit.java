package com.domgarr.RedditClone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Subreddit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Subreddit name is required.")
    private String name;

    @NotBlank(message="Description is required.")
    private String description;

    @OneToMany
    private List<Post> posts;
    private Instant createdDate;

    @ManyToOne
    private User user;
}
