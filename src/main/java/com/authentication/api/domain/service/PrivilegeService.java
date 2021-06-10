package com.authentication.api.domain.service;

import com.authentication.api.infrastructure.persistence.entity.Privilege;
import com.authentication.api.infrastructure.persistence.jpa.PrivilegeJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Privilege service.
 */
@Service
@AllArgsConstructor
public class PrivilegeService {
    private final PrivilegeJpaRepository privilegeJpaRepository;

    /**
     * Get all privileges list.
     *
     * @return the list
     */
    public List<Privilege> getAllPrivileges(){
        return privilegeJpaRepository.findAll();
    }
}
