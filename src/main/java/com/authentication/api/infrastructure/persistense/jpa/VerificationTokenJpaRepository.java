package com.authentication.api.infrastructure.persistense.jpa;

import com.authentication.api.infrastructure.persistense.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The type Verification token jpa repository.
 */
@Repository
public interface VerificationTokenJpaRepository extends JpaRepository<VerificationToken, Long> {

}
