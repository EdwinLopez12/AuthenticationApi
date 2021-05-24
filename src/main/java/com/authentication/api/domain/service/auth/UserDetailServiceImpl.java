package com.authentication.api.domain.service.auth;

import com.authentication.api.infrastructure.persistense.entity.Privilege;
import com.authentication.api.infrastructure.persistense.entity.Role;
import com.authentication.api.infrastructure.persistense.entity.User;
import com.authentication.api.infrastructure.persistense.jpa.UserJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The User detail service.
 */
@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    /**
     * For each request, the username of jwt is obtained
     * then a user with all privileges is returned
     *
     * @param username the string username
     * @return core user
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username){
        Optional<User> userOptional = userJpaRepository.findByUsername(username);
        User user = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("No user found with user name: "+username));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                user.getIsEnable(),
                true, true, true,
                getAuthorities(user.getRoles()));
    }

    /**
     * For each role that the user has, the privileges are obtained
     * @param roles the roles list
     * @return the authorities list
     */
    private Collection<? extends GrantedAuthority> getAuthorities(List<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    /**
     * get all privileges on roles
     *
     * @param roles the roles list
     * @return the privileges
     */
    private List<String> getPrivileges(List<Role> roles) {
        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    /**
     * Create a list for authorities for each privilege
     *
     * @param privileges the privileges list
     * @return the authorities list
     */
    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
