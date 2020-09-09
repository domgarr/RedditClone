package com.domgarr.RedditClone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message="Post name can't be empty or null.")
    private String name;

    @Nullable
    private String url;

    @Nullable
    @Lob
    private String description;

    private Integer voteCount;

    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName = "id")
    private User user;

    private Instant createdDate;

    @ManyToOne
    @JoinColumn(name="subredditId", referencedColumnName = "id")
    private Subreddit subreddit;


}
