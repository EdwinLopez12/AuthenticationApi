package com.authentication.api.infrastructure.controller;

import com.authentication.api.domain.dto.PrivilegeRequest;
import com.authentication.api.domain.dto.RoleRequest;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Role controller test.
 */
@SpringBootTest
@AutoConfigureMockMvc
class RoleControllerTest {

    private static final String USERNAME_ADMIN_PRIVILEGES = "ADMIN";
    private static final String USERNAME_BASIC_PRIVILEGES = "USER";
    private static final String PASSWORD_PLAIN = "123456";
    private static final String URL_ROLES = "http://localhost:9090/api/roles";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtProvider jwtProvider;
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
     * Gets all roles throw exception if user has not browse role privilege.
     *
     * @throws Exception the exception
     */
    @Test
    void get_all_roles_throw_exception_if_user_has_not_browse_role_privilege() throws Exception {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME_BASIC_PRIVILEGES, PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(get(URL_ROLES)
            .header("Authorization", "Bearer "+token)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andReturn().getResponse();
        assertEquals(401, response.getStatus());
    }

    /**
     * Gets all roles return data if user has browse role privilege.
     *
     * @throws Exception the exception
     */
    @Test
    void get_all_roles_return_data_if_user_has_browse_role_privilege() throws Exception {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME_ADMIN_PRIVILEGES, PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(get(URL_ROLES)
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
    }

    /**
     * Gets role details throw exception if user has not read role privilege.
     *
     * @throws Exception the exception
     */
    @Test
    void get_role_details_throw_exception_if_user_has_not_read_role_privilege() throws Exception{
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME_BASIC_PRIVILEGES, PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(get(URL_ROLES+"/1")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse();
        assertEquals(401, response.getStatus());
    }

    /**
     * Gets role details throw exception if role not exist.
     *
     * @throws Exception the exception
     */
    @Test
    void get_role_details_throw_exception_if_role_not_exist() throws Exception{
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME_ADMIN_PRIVILEGES, PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(get(URL_ROLES+"/10")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertEquals(404, response.getStatus());
    }

    /**
     * Gets role details return data if user has read role privilege.
     *
     * @throws Exception the exception
     */
    @Test
    void get_role_details_return_data_if_user_has_read_role_privilege() throws Exception {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME_ADMIN_PRIVILEGES, PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(get(URL_ROLES+"/1")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
    }

    /**
     * Create role throw exception if user has not add role privilege.
     *
     * @throws Exception the exception
     */
    @Test
    void create_role_throw_exception_if_user_has_not_add_role_privilege() throws Exception {
        List<PrivilegeRequest> privilegeRequestList = new ArrayList<>();
        PrivilegeRequest privilegeRequest = PrivilegeRequest.builder()
                .name("ADD_ROLE")
                .build();
        privilegeRequestList.add(privilegeRequest);

        RoleRequest roleRequest = RoleRequest.builder()
                .name("new role")
                .privileges(privilegeRequestList)
                .build();

        String postValue = objectMapper.writeValueAsString(roleRequest);

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME_BASIC_PRIVILEGES, PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(post(URL_ROLES)
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(postValue))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse();
        assertEquals(401, response.getStatus());
    }

    /**
     * Create role throws exception if data is invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void create_role_throws_exception_if_data_is_invalid() throws Exception {
        List<PrivilegeRequest> privilegeRequestList = new ArrayList<>();
        PrivilegeRequest privilegeRequest = new PrivilegeRequest();
        privilegeRequestList.add(privilegeRequest);

        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setPrivileges(privilegeRequestList);

        String postValue = objectMapper.writeValueAsString(roleRequest);

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME_ADMIN_PRIVILEGES, PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(post(URL_ROLES)
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(postValue))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();
        assertEquals(422, response.getStatus());
    }

    /**
     * Create role throws exception if role already exist.
     *
     * @throws Exception the exception
     */
    @Test
    void create_role_throws_exception_if_role_already_exist() throws Exception {
        List<PrivilegeRequest> privilegeRequestList = new ArrayList<>();
        PrivilegeRequest privilegeRequest = PrivilegeRequest.builder()
                .name("ADD_ROLE")
                .build();
        privilegeRequestList.add(privilegeRequest);

        RoleRequest roleRequest = RoleRequest.builder()
                .name("ADMIN_ROLE")
                .privileges(privilegeRequestList)
                .build();

        String postValue = objectMapper.writeValueAsString(roleRequest);

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME_ADMIN_PRIVILEGES, PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(post(URL_ROLES)
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(postValue))
                .andExpect(status().isConflict())
                .andReturn().getResponse();
        assertEquals(409, response.getStatus());
    }

    /**
     * Create role if user has add role privilege and data is valid.
     *
     * @throws Exception the exception
     */
    @Test
    void create_role_if_user_has_add_role_privilege_and_data_is_valid() throws Exception {
        List<PrivilegeRequest> privilegeRequestList = new ArrayList<>();
        PrivilegeRequest privilegeRequest = PrivilegeRequest.builder()
                .name("ADD_ROLE")
                .build();
        privilegeRequestList.add(privilegeRequest);

        RoleRequest roleRequest = RoleRequest.builder()
                .name("new role")
                .privileges(privilegeRequestList)
                .build();

        String postValue = objectMapper.writeValueAsString(roleRequest);

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME_ADMIN_PRIVILEGES, PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(post(URL_ROLES)
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(postValue))
                .andExpect(status().isCreated())
                .andReturn().getResponse();
        assertEquals(201, response.getStatus());
    }

    /**
     * Update role throw exception if user has not edit role privilege.
     *
     * @throws Exception the exception
     */
    @Test
    void update_role_throw_exception_if_user_has_not_edit_role_privilege() throws Exception{
        List<PrivilegeRequest> privilegeRequestList = new ArrayList<>();
        PrivilegeRequest privilegeRequest = PrivilegeRequest.builder()
                .name("ADD_ROLE")
                .build();
        privilegeRequestList.add(privilegeRequest);

        RoleRequest roleRequest = RoleRequest.builder()
                .name("new role")
                .privileges(privilegeRequestList)
                .build();

        String postValue = objectMapper.writeValueAsString(roleRequest);

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME_BASIC_PRIVILEGES, PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(put(URL_ROLES+"/3")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(postValue))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse();
        assertEquals(401, response.getStatus());
    }

    /**
     * Update role if user has edit role privilege and data is valid.
     *
     * @throws Exception the exception
     */
    @Test
    void update_role_if_user_has_edit_role_privilege_and_data_is_valid() throws Exception{
        List<PrivilegeRequest> privilegeRequestList = new ArrayList<>();
        PrivilegeRequest privilegeRequest = PrivilegeRequest.builder()
                .name("ADD_ROLE")
                .build();
        privilegeRequestList.add(privilegeRequest);

        RoleRequest roleRequest = RoleRequest.builder()
                .name("new role test")
                .privileges(privilegeRequestList)
                .build();

        String postValue = objectMapper.writeValueAsString(roleRequest);

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME_ADMIN_PRIVILEGES, PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(put(URL_ROLES+"/3")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(postValue))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
    }

    /**
     * Delete role throw exception if user has not delete role privilege.
     *
     * @throws Exception the exception
     */
    @Test
    void delete_role_throw_exception_if_user_has_not_delete_role_privilege() throws Exception {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME_BASIC_PRIVILEGES, PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(delete(URL_ROLES+"/3")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse();
        assertEquals(401, response.getStatus());
    }

    /**
     * Delete role throw exception if role not exist.
     *
     * @throws Exception the exception
     */
    @Test
    void delete_role_throw_exception_if_role_not_exist() throws Exception {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME_ADMIN_PRIVILEGES, PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(delete(URL_ROLES+"/10")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertEquals(404, response.getStatus());
    }

    /**
     * Delete role throw exception if role has user.
     *
     * @throws Exception the exception
     */
    @Test
    void delete_role_throw_exception_if_role_has_user() throws Exception {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME_ADMIN_PRIVILEGES, PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        MockHttpServletResponse response = mockMvc.perform(delete(URL_ROLES+"/2")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn().getResponse();
        assertEquals(500, response.getStatus());
    }

    /**
     * Delete role if user has delete role privilege.
     *
     * @throws Exception the exception
     */
    @Test
    void delete_role_if_user_has_delete_role_privilege() throws Exception{
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME_ADMIN_PRIVILEGES, PASSWORD_PLAIN));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        Integer body = mockMvc.perform(delete(URL_ROLES+"/3")
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getStatus();
        assertEquals(200, body);
    }
}