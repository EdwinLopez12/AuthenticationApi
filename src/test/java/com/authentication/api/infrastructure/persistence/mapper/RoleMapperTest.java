package com.authentication.api.infrastructure.persistence.mapper;

import com.authentication.api.domain.dto.role.PrivilegeRequest;
import com.authentication.api.domain.dto.role.RoleRequest;
import com.authentication.api.domain.dto.role.RoleResponse;
import com.authentication.api.infrastructure.persistence.entity.Privilege;
import com.authentication.api.infrastructure.persistence.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The type Role mapper test.
 */
@SpringBootTest
class RoleMapperTest {

    private final Privilege privilege = new Privilege();
    private final List<Privilege> privilegeList = new ArrayList<>();
    private final RoleMapper mapper = new RoleMapper();

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        privilege.setId(1L);
        privilege.setName("ADD_USERS");
        privilegeList.add(privilege);
    }

    /**
     * To dto.
     */
    @Test
    void toDto() {
        Role role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .privileges(privilegeList)
                .build();

        RoleResponse dto = mapper.toDto(role);

        assertEquals(role.getName(), dto.getName());
        assertEquals(privilegeList, dto.getPrivileges());
    }

    /**
     * To entity.
     */
    @Test
    void toEntity() {
        PrivilegeRequest privilegeRequest = PrivilegeRequest.builder()
                .name(privilege.getName())
                .build();
        List<PrivilegeRequest> privilegeRequestList = new ArrayList<>();
        privilegeRequestList.add(privilegeRequest);

        RoleRequest roleRequest = RoleRequest.builder()
                .name("ADMIN")
                .privileges(privilegeRequestList)
                .build();

        Role role = mapper.toEntity(1L, roleRequest, privilegeList);
        assertEquals(role.getName(), roleRequest.getName());
        assertEquals(role.getPrivileges(), privilegeList);
    }
}