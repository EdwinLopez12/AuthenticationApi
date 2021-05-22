package com.authentication.api.infrastructure.persistense.mapper;

import com.authentication.api.domain.dto.UserResponse;
import com.authentication.api.infrastructure.persistense.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();


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
}