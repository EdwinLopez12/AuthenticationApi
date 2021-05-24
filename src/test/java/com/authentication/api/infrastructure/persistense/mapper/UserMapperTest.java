package com.authentication.api.infrastructure.persistense.mapper;

import com.authentication.api.domain.dto.UserResponse;
import com.authentication.api.domain.dto.auth.PasswordResetRequest;
import com.authentication.api.infrastructure.persistense.entity.Privilege;
import com.authentication.api.infrastructure.persistense.entity.Role;
import com.authentication.api.infrastructure.persistense.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The type User mapper test.
 */
@SpringBootTest
class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();


    /**
     * To dto.
     */
    @Test
    void toDto() {
        User user = User.builder()
                .username("User mapper Test")
                .email("usermapper@mail.com")
                .build();
        UserResponse dto = userMapper.toDto(user);
        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.getEmail(), dto.getEmail());
    }

    /**
     * To entity.
     */
    @Test
    void toEntity() {
        List<Privilege> privilegeList = new ArrayList<>();
        List<Role> roleList = new ArrayList<>();
        Privilege privilege = new Privilege();
        privilege.setId(1L);
        privilege.setName("ADD_USERS");
        privilegeList.add(privilege);

        Role role = Role.builder()
                .id(1L)
                .name("ADMIN")
                .privileges(privilegeList)
                .build();
        roleList.add(role);

        PasswordResetRequest passwordResetRequest = PasswordResetRequest.builder()
                .password("123456")
                .passwordVerify("123456")
                .build();

        User user = userMapper.toEntity(1L, "User mapper Test", true, roleList, passwordResetRequest);
        assertEquals("User mapper Test", user.getUsername());
        assertEquals(passwordResetRequest.getEmail(), user.getEmail());
        assertEquals(passwordResetRequest.getPassword(), user.getPassword());
        assertEquals(true, user.getIsEnable());
        assertEquals(roleList, user.getRoles());
    }
}