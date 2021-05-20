package com.authentication.api.infrastructure.persistense.jpa;

import com.authentication.api.infrastructure.persistense.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Role jpa repository.
 */
@Repository
public interface RoleJpaRepository extends JpaRepository<Role, Long> {
}
