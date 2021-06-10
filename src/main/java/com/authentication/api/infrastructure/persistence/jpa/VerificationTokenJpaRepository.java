package com.authentication.api.infrastructure.persistence.jpa;

import com.authentication.api.infrastructure.persistence.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The type Verification token jpa repository.
 */
@Repository
public interface VerificationTokenJpaRepository extends JpaRepository<VerificationToken, Long> {

    /**
     * Find by token optional.
     *
     * @param token the token
     * @return the optional
     */
    Optional<VerificationToken> findByToken(String token);
}
