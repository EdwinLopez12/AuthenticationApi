package com.authentication.api.infrastructure.persistense.jpa;

import com.authentication.api.infrastructure.persistense.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Privilege jpa repository.
 */
@Repository
public interface PrivilegeJpaRepository extends JpaRepository<Privilege, Long> {

    /**
     * Find by name privilege.
     *
     * @param name the name
     * @return the privilege
     */
    Privilege findByName(String name);

}
