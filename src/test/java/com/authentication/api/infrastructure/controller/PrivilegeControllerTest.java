package com.authentication.api.infrastructure.controller;

import com.authentication.api.infrastructure.security.JwtAuthenticationFilter;
import com.authentication.api.infrastructure.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Privilege controller test.
 */
@SpringBootTest
@AutoConfigureMockMvc
class PrivilegeControllerTest {

    private static final String USERNAME_ADMIN_PRIVILEGES = "ADMIN";
    private static final String PASSWORD_PLAIN = "123456";
    private static final String USERNAME_BASIC_PRIVILEGES = "USER";
    private static final String URL_PRIVILEGES = "http://localhost:9090/api/privileges";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(jwtAuthenticationFilter)
                .alwaysDo(print())
                .build();
    }

    /**
     * Gets all privileges throws unauthorized exception if user has no privilege.
     *
     * @throws Exception the exception
     */
    @Test
    void get_all_privileges_throws_unauthorized_exception_if_user_has_not_browse_privilege() throws Exception {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME_BASIC_PRIVILEGES, PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(get(URL_PRIVILEGES)
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse();
        assertEquals(401, response.getStatus());
    }

    /**
     * Gets all privileges return data if user has privilege.
     *
     * @throws Exception the exception
     */
    @Test
    void get_all_privileges_return_data_if_user_has_browse_privilege() throws Exception {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME_ADMIN_PRIVILEGES, PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(get(URL_PRIVILEGES)
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
    }
}