package com.authentication.api.infrastructure.persistense.mapper;

import com.authentication.api.domain.dto.role.RoleRequest;
import com.authentication.api.domain.dto.role.RoleResponse;
import com.authentication.api.infrastructure.persistense.entity.Privilege;
import com.authentication.api.infrastructure.persistense.entity.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Role mapper.
 */
@Component
@AllArgsConstructor
public class RoleMapper {

    /**
     * To dto role response.
     *
     * @param role the role
     * @return the role response
     */
    public RoleResponse toDto(Role role){
        return RoleResponse.builder()
                .name(role.getName())
                .privileges(role.getPrivileges())
                .build();
    }

    /**
     * To entity role.
     *
     * @param id            the id
     * @param roleRequest   the role request
     * @param privilegeList the privilege list
     * @return the role
     */
    public Role toEntity(Long id, RoleRequest roleRequest, List<Privilege> privilegeList){
        return Role.builder()
                .id(id)
                .name(roleRequest.getName())
                .privileges(privilegeList)
                .build();
    }
}
