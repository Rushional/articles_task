package com.rushional.articles_task.security;

import com.rushional.articles_task.models.entities.AppUser;
import com.rushional.articles_task.models.entities.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class AppUserDetails implements UserDetails {
    private AppUser user;

    public AppUserDetails(AppUser user) {
        this.user = user;
    }

    @Override
//    @Transactional
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authoritiesList = new ArrayList<>();

        for (Role role : user.getRoles()) {
            authoritiesList.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }

        return authoritiesList;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
