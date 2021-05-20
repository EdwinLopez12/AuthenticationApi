package com.authentication.api.infrastructure.persistense.mapper;

import com.authentication.api.domain.dto.PrivilegeRequest;
import com.authentication.api.domain.dto.RoleRequest;
import com.authentication.api.domain.dto.RoleResponse;
import com.authentication.api.infrastructure.persistense.entity.Privilege;
import com.authentication.api.infrastructure.persistense.entity.Role;
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
        Role role = new Role(1L,"ADMIN", privilegeList);

        RoleResponse dto = mapper.toDto(role);

        assertEquals(role.getName(), dto.getName());
        assertEquals(privilegeList, dto.getPrivileges());
    }

    /**
     * To entity.
     */
    @Test
    void toEntity() {
        PrivilegeRequest privilegeRequest = new PrivilegeRequest();
        privilegeRequest.setName(privilege.getName());
        List<PrivilegeRequest> privilegeRequestList = new ArrayList<>();
        privilegeRequestList.add(privilegeRequest);

        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setName("ADMIN");
        roleRequest.setPrivileges(privilegeRequestList);

        Role role = mapper.toEntity(1L, roleRequest, privilegeList);
        assertEquals(role.getName(), roleRequest.getName());
        assertEquals(role.getPrivileges(), privilegeList);
    }
}