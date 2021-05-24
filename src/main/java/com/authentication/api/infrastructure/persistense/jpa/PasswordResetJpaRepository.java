package com.authentication.api.infrastructure.persistense.jpa;

import com.authentication.api.infrastructure.persistense.entity.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * The interface Password reset jpa repository.
 */
@Repository
public interface PasswordResetJpaRepository extends JpaRepository<PasswordReset, Long> {

    /**
     * Find by token optional.
     *
     * @param token the token
     * @return the optional
     */
    Optional<PasswordReset> findByToken(String token);

    /**
     * Delete by email.
     *
     * @param email the email
     */
    @Transactional
    void deleteByEmail(String email);
}
