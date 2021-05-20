package com.authentication.api.infrastructure.persistense.jpa;

import com.authentication.api.infrastructure.persistense.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface User jpa repository.
 */
@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

    /**
     * Find by username optional.
     *
     * @param username the username
     * @return the optional
     */
    Optional<User> findByUsername(String username);

    /**
     * Find by email optional.
     *
     * @param email the email
     * @return the optional
     */
    Optional<User> findByEmail(String email);
}
