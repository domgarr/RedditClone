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
@NamedEntityGraph(name = "comment.user",
attributeNodes = @NamedAttributeNode("user"))
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Text is required.")
    private String text;

    //The default to @ManyToOne and @OneToOne are EAGER (For JPA) for Hibernate all are LAZY.
    //TODO: Set @ManyToOne relationship to LAZY and use @NamedEntityGraph to eager load when needed.
    //TODO: Post has a User reference and Comment has a user reference, but User in Post isn't loaded.
    @ManyToOne
    @JoinColumn(name="postId", referencedColumnName = "id")
    private Post post;
    private Instant createdDate;

    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName = "id")
    private User user;


}
