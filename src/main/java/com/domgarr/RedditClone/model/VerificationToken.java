package com.domgarr.RedditClone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String token;

    @OneToOne
    private User user;
    private Instant expiryDate;
}
