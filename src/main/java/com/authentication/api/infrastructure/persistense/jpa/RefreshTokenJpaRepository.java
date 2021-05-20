package com.authentication.api.infrastructure.persistense.jpa;

import com.authentication.api.infrastructure.persistense.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Refresh token jpa repository.
 */
@Repository
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {

}
