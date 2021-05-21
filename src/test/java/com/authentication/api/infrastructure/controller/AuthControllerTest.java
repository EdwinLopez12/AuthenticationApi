package com.authentication.api.infrastructure.controller;

import com.authentication.api.domain.dto.auth.LoginUserRequest;
import com.authentication.api.domain.dto.auth.LogoutUserRequest;
import com.authentication.api.domain.dto.auth.RefreshTokenRequest;
import com.authentication.api.domain.dto.auth.RegisterUserRequest;
import com.authentication.api.infrastructure.persistense.entity.VerificationToken;
import com.authentication.api.infrastructure.persistense.jpa.VerificationTokenJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Auth controller test.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    // signup data valid
    private static final String EMAIL_REGISTER_TEST = "test@mail.com";
    private static final String USERNAME_REGISTER_TEST = "test";
    private static final String PASSWORD_PLAIN = "123456";
    // signup data invalid
    private static final String EMAIL_REGISTER_TEST_INVALID = "@mail.com";
    private static final String USERNAME_REGISTER_TEST_INVALID = "tt";
    private static final String PASSWORD_INVALID = "1234";
    // Login no exist
    private static final String USERNAME_NOT_EXIST = "otherUser";
    // user data with user privileges
    private static final String USERNAME_BASIC_PRIVILEGES = "USER";
    private static final String EMAIL_USER_PRIVILEGES = "USER@mail.com";
    private static final String REFRESH_TOKEN_USER_PRIVILEGES = "e8818837-d76c-4c95-bc72-edc9fdcc693d-USER";
    private static final String VERIFICATION_TOKEN_USER_PRIVILEGES = "2d5eff8a-16c0-46f7-a66f-9aebe0c0388d-USER";

    private static final String URL_AUTH = "http://localhost:9090/api/auth/";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private VerificationTokenJpaRepository verificationTokenJPARepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();
    }

    /**
     * Signup return status unprocessable entity if data is invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void signup_return_status_unprocessable_entity_if_data_is_invalid() throws Exception{
        RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
                .username(USERNAME_REGISTER_TEST_INVALID)
                .password(PASSWORD_INVALID)
                .email(EMAIL_REGISTER_TEST_INVALID)
                .build();

        String postValue = objectMapper.writeValueAsString(registerUserRequest);

        MockHttpServletResponse response = mockMvc.perform(post(URL_AUTH+ "signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(postValue))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();
        assertEquals(422, response.getStatus());
    }

    /**
     * Signup return status conflict if username is already use.
     *
     * @throws Exception the exception
     */
    @Test
    void signup_return_status_conflict_if_username_is_already_use() throws Exception{
        RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
                .username(USERNAME_BASIC_PRIVILEGES)
                .password(PASSWORD_PLAIN)
                .email(EMAIL_REGISTER_TEST)
                .build();

        String postValue = objectMapper.writeValueAsString(registerUserRequest);

        MockHttpServletResponse response = mockMvc.perform(post(URL_AUTH+ "signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(postValue))
                .andExpect(status().isConflict())
                .andReturn().getResponse();
        assertEquals(409, response.getStatus());
    }

    /**
     * Signup return status conflict if email is already use.
     *
     * @throws Exception the exception
     */
    @Test
    void signup_return_status_conflict_if_email_is_already_use() throws Exception{
        RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
                .username(USERNAME_REGISTER_TEST)
                .password(PASSWORD_PLAIN)
                .email(EMAIL_USER_PRIVILEGES)
                .build();

        String postValue = objectMapper.writeValueAsString(registerUserRequest);

        MockHttpServletResponse response = mockMvc.perform(post(URL_AUTH+ "signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(postValue))
                .andExpect(status().isConflict())
                .andReturn().getResponse();
        assertEquals(409, response.getStatus());
    }

    /**
     * Signup return status ok if data is valid.
     *
     * @throws Exception the exception
     */
    @Test
    void signup_return_status_ok_if_data_is_valid() throws Exception{
        RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
                .username(USERNAME_REGISTER_TEST)
                .password(PASSWORD_PLAIN)
                .email(EMAIL_REGISTER_TEST)
                .build();

        String postValue = objectMapper.writeValueAsString(registerUserRequest);

        MockHttpServletResponse response = mockMvc.perform(post(URL_AUTH+ "signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(postValue))
                .andExpect(content().string("User register successfully"))
                .andExpect(status().isCreated())
                .andReturn().getResponse();
        assertEquals("User register successfully", response.getContentAsString());
        assertEquals(201, response.getStatus());
    }

    /**
     * Verify account return status internal server error if token is invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void verifyAccount_return_status_internal_server_error_if_token_is_invalid() throws Exception{
        Optional<VerificationToken> token = verificationTokenJPARepository.findByToken(VERIFICATION_TOKEN_USER_PRIVILEGES);
        if(token.isPresent()){
            MockHttpServletResponse response = mockMvc.perform(get(URL_AUTH+ "account-verification/"+token.get().getToken()+"bad_token")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isInternalServerError())
                    .andReturn().getResponse();
            assertEquals(500, response.getStatus());
        }
    }

    /**
     * Verify account return status ok if token is valid.
     *
     * @throws Exception the exception
     */
    @Test
    void verifyAccount_return_status_ok_if_token_is_valid() throws Exception{
        Optional<VerificationToken> token = verificationTokenJPARepository.findByToken(VERIFICATION_TOKEN_USER_PRIVILEGES);
        if(token.isPresent()){
            MockHttpServletResponse response = mockMvc.perform(get(URL_AUTH+ "account-verification/"+token.get().getToken())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(content().string("Account activated successfully"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            assertEquals("Account activated successfully", response.getContentAsString());
            assertEquals(200, response.getStatus());
        }
    }

    /**
     * Login return status unprocessable entity if data is invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void login_return_status_unprocessable_entity_if_data_is_invalid() throws Exception{
        LoginUserRequest loginUserRequest = LoginUserRequest.builder()
                .username(USERNAME_REGISTER_TEST_INVALID)
                .password(PASSWORD_INVALID)
                .build();

        String postValue = objectMapper.writeValueAsString(loginUserRequest);

        MockHttpServletResponse response = mockMvc.perform(post(URL_AUTH+ "login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(postValue))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();
        assertEquals(422, response.getStatus());
    }

    /**
     * Login return status unauthorized if user no exist.
     *
     * @throws Exception the exception
     */
    @Test
    void login_return_status_unauthorized_if_user_no_exist() throws Exception{
        LoginUserRequest loginUserRequest = LoginUserRequest.builder()
                .username(USERNAME_NOT_EXIST)
                .password(PASSWORD_PLAIN)
                .build();

        String postValue = objectMapper.writeValueAsString(loginUserRequest);

        MockHttpServletResponse response = mockMvc.perform(post(URL_AUTH+ "login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(postValue))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse();
        assertEquals(401, response.getStatus());
    }

    /**
     * Login return status ok if data is valid.
     *
     * @throws Exception the exception
     */
    @Test
    void login_return_status_ok_if_data_is_valid() throws Exception{
        LoginUserRequest loginUserRequest = LoginUserRequest.builder()
                .username(USERNAME_BASIC_PRIVILEGES)
                .password(PASSWORD_PLAIN)
                .build();

        String postValue = objectMapper.writeValueAsString(loginUserRequest);

        MockHttpServletResponse response = mockMvc.perform(post(URL_AUTH+ "login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(postValue))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
    }

    /**
     * Refresh tokens return status internal server error if refresh token is invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void refreshTokens_return_status_internal_server_error_if_refresh_token_is_invalid() throws Exception{
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken(REFRESH_TOKEN_USER_PRIVILEGES+"bad-refresh-token")
                .username(USERNAME_BASIC_PRIVILEGES)
                .build();

        String postValue = objectMapper.writeValueAsString(refreshTokenRequest);

        MockHttpServletResponse response = mockMvc.perform(post(URL_AUTH+ "refresh/token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(postValue))
                .andExpect(status().isInternalServerError())
                .andReturn().getResponse();
        assertEquals(500, response.getStatus());
    }

    /**
     * Refresh tokens return status unauthorized if username not exist.
     *
     * @throws Exception the exception
     */
    @Test
    void refreshTokens_return_status_unauthorized_if_username_not_exist() throws Exception{
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken(REFRESH_TOKEN_USER_PRIVILEGES)
                .username(USERNAME_NOT_EXIST)
                .build();

        String postValue = objectMapper.writeValueAsString(refreshTokenRequest);

        MockHttpServletResponse response = mockMvc.perform(post(URL_AUTH+ "refresh/token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(postValue))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse();
        assertEquals(401, response.getStatus());
    }

    /**
     * Refresh tokens return status ok and new jwt if data is valid.
     *
     * @throws Exception the exception
     */
    @Test
    void refreshTokens_return_status_ok_and_new_JWT_if_data_is_valid() throws Exception{
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken(REFRESH_TOKEN_USER_PRIVILEGES)
                .username(USERNAME_BASIC_PRIVILEGES)
                .build();
        String postValue = objectMapper.writeValueAsString(refreshTokenRequest);

        Integer body = mockMvc.perform(post(URL_AUTH+ "refresh/token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(postValue)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getStatus();
        assertEquals(200, body);
    }

    /**
     * Logout return status internal server error if refresh token is invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void logout_return_status_internal_server_error_if_refresh_token_is_invalid() throws Exception{
        LogoutUserRequest logoutUserRequest = LogoutUserRequest.builder()
                .refreshToken(REFRESH_TOKEN_USER_PRIVILEGES+"bad-refresh-token")
                .build();

        String postValue = objectMapper.writeValueAsString(logoutUserRequest);

        MockHttpServletResponse response = mockMvc.perform(post(URL_AUTH+ "logout")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(postValue)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andReturn().getResponse();
        assertEquals(500, response.getStatus());
    }

    /**
     * Logout return status ok if data is valid.
     *
     * @throws Exception the exception
     */
    @Test
    void logout_return_status_ok_if_data_is_valid() throws Exception{
        LogoutUserRequest logoutUserRequest = LogoutUserRequest.builder()
                .refreshToken(REFRESH_TOKEN_USER_PRIVILEGES)
                .build();

        String postValue = objectMapper.writeValueAsString(logoutUserRequest);

        MockHttpServletResponse response = mockMvc.perform(post(URL_AUTH+ "logout")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(postValue)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string("Refresh token deleted successfully"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertEquals("Refresh token deleted successfully", response.getContentAsString());
        assertEquals(200, response.getStatus());
    }
}