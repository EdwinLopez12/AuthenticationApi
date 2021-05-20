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

    private Collection<? extends GrantedAuthority> getAuthorities(List<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

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

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
