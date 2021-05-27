package com.authentication.api.infrastructure.persistense.jpa;

import com.authentication.api.infrastructure.persistense.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The interface Role jpa repository.
 */
@Repository
public interface RoleJpaRepository extends JpaRepository<Role, Long> {

    /**
     * Find by name object.
     *
     * @param user the user
     * @return the object
     */
    Role findByName(String user);

    /**
     * Count user roles long.
     *
     * @param name the name
     * @return the long
     */
    @Query( nativeQuery = true,
            value = "SELECT COUNT(*) FROM tb_user " +
                    "INNER JOIN tb_users_roles on tb_user.id = tb_users_roles.user_id " +
                    "INNER JOIN tb_role on tb_users_roles.role_id = tb_role.id " +
                    "WHERE tb_role.name = ?")
    Long countUserRoles(@Param("name") String name);
}
