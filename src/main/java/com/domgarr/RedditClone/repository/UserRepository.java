package com.domgarr.RedditClone.repository;

import com.domgarr.RedditClone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    /*
    Using count(1) returns a BigInteger,
    I can out a way to return 1 or 0 using if/else in sql? But for now I will return A BigInteger.
     */

    @Query(value = "SELECT count(1) from user where email = :email AND enabled = 1", nativeQuery = true)
    BigInteger existsByEmailAndEnabled(String email);
}
