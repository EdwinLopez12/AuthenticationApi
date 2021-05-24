package com.authentication.api.infrastructure.controller;

import com.authentication.api.domain.service.PrivilegeService;
import com.authentication.api.infrastructure.persistense.entity.Privilege;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type Privilege controller.
 */
@RestController
@RequestMapping("/api/privileges")
@AllArgsConstructor
public class PrivilegeController {
    private final PrivilegeService privilegeService;

    /**
     * Get all privileges response entity.
     *
     * @return the response entity
     */
    @ApiOperation("Get all privileges data")
    @PreAuthorize("hasAuthority('BROWSE_PRIVILEGE')")
    @GetMapping
    public ResponseEntity<List<Privilege>> getAllPrivileges(){
        return new ResponseEntity<>(privilegeService.getAllPrivileges(), HttpStatus.OK);
    }
}
