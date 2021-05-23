package com.authentication.api.infrastructure.persistense.mapper;

import com.authentication.api.domain.dto.UserResponse;
import com.authentication.api.domain.dto.auth.PasswordResetRequest;
import com.authentication.api.infrastructure.persistense.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
        PasswordResetRequest passwordResetRequest = PasswordResetRequest.builder()
                .password("123456")
                .passwordVerify("123456")
                .build();

        User user = userMapper.toEntity(1L, "User mapper Test", passwordResetRequest);
        assertEquals("User mapper Test", user.getUsername());
        assertEquals(passwordResetRequest.getEmail(), user.getEmail());
        assertEquals(passwordResetRequest.getPassword(), user.getPassword());
    }
}