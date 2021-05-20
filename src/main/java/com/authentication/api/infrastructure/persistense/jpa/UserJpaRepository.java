package com.authentication.api.infrastructure.persistense.jpa;

import com.authentication.api.infrastructure.persistense.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface User jpa repository.
 */
@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

}
