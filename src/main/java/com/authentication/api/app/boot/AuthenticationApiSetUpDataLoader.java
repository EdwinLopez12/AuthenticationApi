package com.authentication.api.app.boot;

import com.authentication.api.infrastructure.persistense.entity.*;
import com.authentication.api.infrastructure.persistense.jpa.*;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The type Authentication api set up data loader.
 */
@Component
@AllArgsConstructor
public class AuthenticationApiSetUpDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * The Already setup.
     */
    boolean alreadySetup;

    private final PrivilegeJpaRepository privilegeJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final VerificationTokenJpaRepository verificationTokenJpaRepository;
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(alreadySetup){
            return;
        }
        // BREAD to role
        Privilege browseRole = createPrivilegeIfNotFound("BROWSE_ROLE");
        Privilege readRole = createPrivilegeIfNotFound("READ_ROLE");
        Privilege editRole = createPrivilegeIfNotFound("EDIT_ROLE");
        Privilege addRole = createPrivilegeIfNotFound("ADD_ROLE");
        Privilege deleteRole = createPrivilegeIfNotFound("DELETE_ROLE");

        // BR--- to privilege
        Privilege browsePrivilege = createPrivilegeIfNotFound("BROWSE_PRIVILEGE");
        Privilege readPrivilege = createPrivilegeIfNotFound("READ_ROLE");

        // TEST roles
        Privilege testBrowsePrivilege = createPrivilegeIfNotFound("TEST_BROWSE");

        List<Privilege> adminPrivileges = Arrays.asList(browseRole, readRole, editRole, addRole, deleteRole, browsePrivilege, readPrivilege);
        List<Privilege> userPrivileges = Collections.singletonList(testBrowsePrivilege);

        createRoleIfNoExist("ADMIN_ROLE", adminPrivileges);
        createRoleIfNoExist("TEST_ROLE", userPrivileges);

        createUserIfNoFound("ADMIN", "ADMIN_ROLE");
        createUserIfNoFound("TEST", "TEST_ROLE");

        alreadySetup = true;
    }

    /**
     * Create role if no exist.
     *
     * @param name       the name
     * @param privileges the privileges
     */
    public void createRoleIfNoExist(String name, List<Privilege> privileges) {
        Role role = roleJpaRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            roleJpaRepository.save(role);
        }
    }

    /**
     * Create privilege if not found privilege.
     *
     * @param privilegeName the privilege name
     * @return the privilege
     */
    @Transactional
    public Privilege createPrivilegeIfNotFound(String privilegeName) {
        Privilege privilege = privilegeJpaRepository.findByName(privilegeName);
        if(privilege == null){
            privilege = new Privilege();
            privilege.setName(privilegeName);
            privilegeJpaRepository.save(privilege);
        }
        return privilege;
    }

    /**
     * Create user if no found.
     *
     * @param name the name
     */
    @Transactional
    public void createUserIfNoFound(String name, String rol){
        List<Role> role = Collections.singletonList(roleJpaRepository.findByName(rol));
        Optional<User> userSearch = userJpaRepository.findByUsername(name);
        if(!userSearch.isPresent()){
            User user = new User();
            user.setUsername(name);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setEmail(name+"@mail.com");
            user.setRoles(role);
            user.setIsEnable(true);
            userJpaRepository.save(user);

            VerificationToken verificationToken = new VerificationToken();
            verificationToken.setToken("2d5eff8a-16c0-46f7-a66f-9aebe0c0388d-"+name);
            verificationToken.setUser(user);
            verificationTokenJpaRepository.save(verificationToken);

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken("e8818837-d76c-4c95-bc72-edc9fdcc693d-"+name);
            refreshToken.setCreatedAt(Instant.now());
            refreshTokenJpaRepository.save(refreshToken);
        }
    }
}
