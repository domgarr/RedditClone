package com.domgarr.RedditClone.repository;

import com.domgarr.RedditClone.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
}
