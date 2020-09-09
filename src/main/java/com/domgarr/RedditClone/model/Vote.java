package com.domgarr.RedditClone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private VoteType voteType;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "postId", referencedColumnName = "id")
    private Post post;

    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName = "id")
    private User user;
}
