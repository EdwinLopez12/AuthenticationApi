package com.authentication.api.infrastructure.persistense.entity;

import lombok.*;

import javax.persistence.*;

import java.time.Instant;

import static javax.persistence.FetchType.LAZY;

/**
 * The Verification token.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @OneToOne(fetch = LAZY)
    private User user;
    private Instant expiryDate;
}
