package com.authentication.api.domain.service.auth;

import com.authentication.api.domain.dto.auth.RegisterUserRequest;
import com.authentication.api.domain.exception.ApiConflict;
import com.authentication.api.domain.service.mail.MailService;
import com.authentication.api.infrastructure.persistense.entity.User;
import com.authentication.api.infrastructure.persistense.entity.VerificationToken;
import com.authentication.api.infrastructure.persistense.jpa.RoleJpaRepository;
import com.authentication.api.infrastructure.persistense.jpa.UserJpaRepository;
import com.authentication.api.infrastructure.persistense.jpa.VerificationTokenJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * The type Auth service.
 */
@Service
@AllArgsConstructor
public class AuthService {

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final VerificationTokenJpaRepository verificationTokenJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    /**
     * Signup.
     *
     * @param registerUserRequest the register user request
     */
    @Transactional
    public void signup(RegisterUserRequest registerUserRequest) {
        Optional<User> usernameExist = userJpaRepository.findByUsername(registerUserRequest.getUsername());
        Optional<User> emailExist = userJpaRepository.findByEmail(registerUserRequest.getEmail());
        if(!usernameExist.isPresent()){
            if(!emailExist.isPresent()){
                User user = User.builder()
                        .username(registerUserRequest.getUsername())
                        .email(registerUserRequest.getEmail())
                        .password(passwordEncoder.encode(registerUserRequest.getPassword()))
                        .isEnable(false)
                        .roles(Collections.singletonList(roleJpaRepository.findByName("USER")))
                        .build();
                String token = generateVerificationToken(user);
                userJpaRepository.save(user);
                mailService.setUpEmailData(user.getEmail(), token);
            }else{
                throw new ApiConflict("Email already exist");
            }
        }else{
            throw new ApiConflict("Username already exist");
        }
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .build();
        verificationTokenJpaRepository.save(verificationToken);
        return token;
    }
}
