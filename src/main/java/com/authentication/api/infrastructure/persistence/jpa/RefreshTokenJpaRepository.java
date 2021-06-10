package com.authentication.api.infrastructure.persistence.jpa;

import com.authentication.api.infrastructure.persistence.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Refresh token jpa repository.
 */
@Repository
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {
    /**
     * Find by token optional.
     *
     * @param token the token
     * @return the optional
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Delete by token.
     *
     * @param token the token
     */
    void deleteByToken(String token);

}
