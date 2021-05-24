package com.authentication.api.domain.service;

import com.authentication.api.domain.dto.PrivilegeRequest;
import com.authentication.api.domain.dto.RoleRequest;
import com.authentication.api.domain.dto.RoleResponse;
import com.authentication.api.domain.exception.ApiConflict;
import com.authentication.api.domain.exception.ApiNotFound;
import com.authentication.api.domain.exception.AuthenticationApiException;
import com.authentication.api.infrastructure.persistense.entity.Privilege;
import com.authentication.api.infrastructure.persistense.entity.Role;
import com.authentication.api.infrastructure.persistense.jpa.PrivilegeJpaRepository;
import com.authentication.api.infrastructure.persistense.jpa.RoleJpaRepository;
import com.authentication.api.infrastructure.persistense.mapper.RoleMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Role service.
 */
@Service
@AllArgsConstructor
public class RoleService {

    private final RoleJpaRepository roleJpaRepository;
    private final PrivilegeJpaRepository privilegeJpaRepository;
    private final RoleMapper roleMapper;

    /**
     * Get all roles list.
     *
     * @return the list
     */
    public List<Role> getAllRoles(){
        return roleJpaRepository.findAll();
    }

    /**
     * Get specific role details.
     *
     * @throws ApiNotFound if role can't be found
     * @param id the id
     * @return the optional
     */
    public Optional<Role> getRoleDetails(Long id){
        Optional<Role> role = roleJpaRepository.findById(id);
        if(role.isPresent()){
            return role;
        }else{
            throw new ApiNotFound("Resource not found");
        }
    }

    /**
     * Create role.
     *
     * @throws ApiConflict if role already exist
     * @param roleRequest the role request
     * @return the role response
     */
    public RoleResponse createRole(RoleRequest roleRequest){
        Role searchRole = roleJpaRepository.findByName(roleRequest.getName());
        if(searchRole != null){
            throw new ApiConflict("Role already exist");
        }
        List<Privilege> privilegeList = getRolePrivileges(roleRequest);
        Role role = Role.builder()
                .name(roleRequest.getName())
                .privileges(privilegeList)
                .build();
        roleJpaRepository.save(role);
        return roleMapper.toDto(role);
    }

    /**
     * Update role.
     *
     * @param id          the id
     * @param roleRequest the role request
     * @return the role response
     */
    public RoleResponse updateRole(Long id, RoleRequest roleRequest){
        List<Privilege> privilegeList = getRolePrivileges(roleRequest);
        Role role = roleMapper.toEntity(id, roleRequest, privilegeList);
        roleJpaRepository.save(role);
        return roleMapper.toDto(role);
    }

    /**
     * Delete role.
     *
     * @throws AuthenticationApiException if at less one user uses thr role
     * @throws ApiNotFound if role can't be found
     * @param id the id
     * @return the string
     */
    public String deleteRole(Long id){
        Optional<Role> deleteRole = roleJpaRepository.findById(id);
        if(deleteRole.isPresent()){
            if(roleJpaRepository.countUserRoles(deleteRole.get().getName()) == 0){
                roleJpaRepository.deleteById(id);
                return "Role deleted successfully";
            }else{
                throw new AuthenticationApiException("The role cannot be removed because it is used by at least one user. Try assigning a new role to the user (s) and try deleting again.");
            }
        }else{
            throw new ApiNotFound("Role not found");
        }
    }

    /**
     * Get all privileges by the role
     *
     * @param roleRequest the role request
     * @return the privilege list
     */
    private List<Privilege> getRolePrivileges(RoleRequest roleRequest) {
        List<Privilege> privilegeList = new ArrayList<>();
        for (PrivilegeRequest privilege: roleRequest.getPrivileges() ){
            privilegeList.add(privilegeJpaRepository.findByName(privilege.getName()));
        }
        return privilegeList;
    }
}
