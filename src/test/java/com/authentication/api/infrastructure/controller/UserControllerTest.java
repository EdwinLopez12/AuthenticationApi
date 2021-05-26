package com.authentication.api.infrastructure.controller;

import com.authentication.api.domain.dto.user.UserRequest;
import com.authentication.api.infrastructure.security.JwtAuthenticationFilter;
import com.authentication.api.infrastructure.security.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The User controller test.
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    private static final String EMAIL = "USER@mail.com";
    private static final String PASSWORD = "123456";
    private static final String USER_INVALID = "us";
    private static final String EMAIL_INVALID = "usermail.com";
    private static final String PASSWORD_INVALID = "1245";
    private static final String URL = "http://localhost:9090/api/user";

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
    @Autowired
    private ObjectMapper objectMapper;

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
     * Gets data user return data.
     *
     * @throws Exception the exception
     */
    @Test
    void get_data_user_return_data() throws Exception {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(ADMIN, PASSWORD));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(get(URL+"/account")
            .header("Authorization", "Bearer "+token)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().getResponse();
        assertEquals(200, response.getStatus());
    }

    /**
     * Update user throw unprocessable entity if data is invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void update_user_throw_unprocessable_entity_if_data_is_invalid() throws Exception {
        UserRequest user = UserRequest.builder()
                .username(USER_INVALID)
                .email(EMAIL_INVALID)
                .password(PASSWORD_INVALID)
                .build();
        String postValue = objectMapper.writeValueAsString(user);

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USER, PASSWORD));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(put(URL+"/account")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(postValue))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();
        assertEquals(422, response.getStatus());
    }

    /**
     * Update username if data is valid.
     *
     * @throws Exception the exception
     */
    @Test
    void update_username_if_data_is_valid() throws Exception {
        UserRequest user = UserRequest.builder()
                .username(USER)
                .build();
        String postValue = objectMapper.writeValueAsString(user);

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USER, PASSWORD));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(put(URL+"/account")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(postValue))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
    }

    /**
     * Update email if data is valid.
     *
     * @throws Exception the exception
     */
    @Test
    void update_email_if_data_is_valid() throws Exception {
        UserRequest user = UserRequest.builder()
                .email(EMAIL)
                .build();
        String postValue = objectMapper.writeValueAsString(user);

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USER, PASSWORD));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(put(URL+"/account")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(postValue))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
    }

    /**
     * Update password if data is valid.
     *
     * @throws Exception the exception
     */
    @Test
    void update_password_if_data_is_valid() throws Exception {
        UserRequest user = UserRequest.builder()
                .password(PASSWORD)
                .build();
        String postValue = objectMapper.writeValueAsString(user);

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USER, PASSWORD));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(put(URL+"/account")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(postValue))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
    }


    /**
     * Update user if data is valid.
     *
     * @throws Exception the exception
     */
    @Test
    void update_user_if_data_is_valid() throws Exception {
        UserRequest user = UserRequest.builder()
                .username(USER)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
        String postValue = objectMapper.writeValueAsString(user);

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USER, PASSWORD));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(put(URL+"/account")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(postValue))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
    }
}