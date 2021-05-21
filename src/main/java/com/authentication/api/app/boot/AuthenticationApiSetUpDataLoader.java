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

    private final PrivilegeJpaRepository privilegeJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final VerificationTokenJpaRepository verificationTokenJpaRepository;
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

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
        createRoleIfNoExist("USER_ROLE", userPrivileges);
        createRoleIfNoExist("TEST_ROLE", userPrivileges);

        createUserIfNoFound("ADMIN", "ADMIN_ROLE");
        createUserIfNoFound("USER", "USER_ROLE");

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
            role = Role.builder()
                    .name(name)
                    .privileges(privileges)
                    .build();
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
            privilege = Privilege.builder()
                    .name(privilegeName)
                    .build();
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
            User user = User.builder()
                    .username(name)
                    .password(passwordEncoder.encode("123456"))
                    .email(name+"@mail.com")
                    .roles(role)
                    .isEnable(true)
                    .build();
            userJpaRepository.save(user);

            VerificationToken verificationToken = VerificationToken.builder()
                    .token("2d5eff8a-16c0-46f7-a66f-9aebe0c0388d-"+name)
                    .user(user)
                    .build();
            verificationTokenJpaRepository.save(verificationToken);

            RefreshToken refreshToken = RefreshToken.builder()
                    .token("e8818837-d76c-4c95-bc72-edc9fdcc693d-"+name)
                    .createdAt(Instant.now())
                    .build();
            refreshTokenJpaRepository.save(refreshToken);
        }
    }
}
