package com.authentication.api.infrastructure.controller;

import com.authentication.api.infrastructure.security.JwtAuthenticationFilter;
import com.authentication.api.infrastructure.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Jwt test.
 */
@SpringBootTest
@AutoConfigureMockMvc
class JwtTest {

    private static final String USERNAME_ADMIN_PRIVILEGES = "OWNER";
    private static final String PASSWORD_PLAIN = "123456";
    private static final String URL_TEST_JWT = "http://localhost:9090/api/privileges";

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
     * User without valid bearer throw unauthorized exception.
     *
     * @param bearer the bearer
     * @throws Exception the exception
     */
    @ParameterizedTest
    @ValueSource(strings = {"Bearer", "Beare", "Bearer-"})
    void user_without_valid_bearer_throw_unauthorized_exception(String bearer) throws Exception {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(USERNAME_ADMIN_PRIVILEGES,
                        PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(get(URL_TEST_JWT)
                .header("Authorization", bearer+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse();
        assertEquals("Unauthorized", response.getErrorMessage());
        assertEquals(401, response.getStatus());
    }


    /**
     * User without valid jwt throw unauthorized exception.
     *
     * @throws Exception the exception
     */
    @Test
    void user_without_valid_jwt_throw_unauthorized_exception() throws Exception {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(USERNAME_ADMIN_PRIVILEGES,
                        PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(get(URL_TEST_JWT)
                .header("Authorization", "Bearer "+token+"bad")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse();
        assertEquals("Unauthorized", response.getErrorMessage());
        assertEquals(401, response.getStatus());
    }
}
