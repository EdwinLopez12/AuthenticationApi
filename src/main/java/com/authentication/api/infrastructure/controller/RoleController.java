package com.authentication.api.infrastructure.controller;

import com.authentication.api.domain.dto.RoleRequest;
import com.authentication.api.domain.dto.RoleResponse;
import com.authentication.api.domain.service.RoleService;
import com.authentication.api.infrastructure.persistense.entity.Role;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * The Role controller.
 */
@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * Get all response entity.
     *
     * @return the response entity
     */
    @ApiOperation("Get all roles data")
    @PreAuthorize("hasAuthority('BROWSE_ROLE')")
    @GetMapping()
    public ResponseEntity<List<Role>> getAll(){
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

    /**
     * Gets role details.
     *
     * @param id the id
     * @return the role details
     */
    @ApiOperation("Get a specific role data")
    @PreAuthorize("hasAuthority('READ_ROLE')")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Role>> getRoleDetails(@PathVariable("id") Long id){
        return new ResponseEntity<>(roleService.getRoleDetails(id), HttpStatus.OK);
    }

    /**
     * Create role response entity.
     *
     * @param roleRequest the role request
     * @return the response entity
     */
    @ApiOperation("Create new role")
    @PreAuthorize("hasAuthority('ADD_ROLE')")
    @PostMapping()
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest roleRequest) {
        return new ResponseEntity<>(roleService.createRole(roleRequest), HttpStatus.CREATED);
    }

    /**
     * Update role response entity.
     *
     * @param id          the id
     * @param roleRequest the role request
     * @return the response entity
     */
    @ApiOperation("Edit a specific role")
    @PreAuthorize("hasAuthority('EDIT_ROLE')")
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> updateRole(@Valid @PathVariable("id") Long id, @RequestBody RoleRequest roleRequest){
        return new ResponseEntity<>(roleService.updateRole(id, roleRequest), HttpStatus.OK);
    }

    /**
     * Delete role response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @ApiOperation("Delete a specific role")
    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable("id") Long id){
        return new ResponseEntity<>(roleService.deleteRole(id), HttpStatus.OK);
    }

}
