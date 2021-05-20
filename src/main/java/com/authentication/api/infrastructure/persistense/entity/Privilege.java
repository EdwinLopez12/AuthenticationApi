package com.authentication.api.infrastructure.persistense.entity;

import lombok.*;

import javax.persistence.*;

/**
 * The Privilege.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
}
