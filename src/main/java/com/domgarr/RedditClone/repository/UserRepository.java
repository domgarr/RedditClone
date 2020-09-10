package com.domgarr.RedditClone.repository;

import com.domgarr.RedditClone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
