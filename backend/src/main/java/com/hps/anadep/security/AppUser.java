package com.hps.anadep.security;

import com.hps.anadep.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

public class AppUser implements UserDetails {
    private UUID id;
    private Long githubUserId;
    private String login;

    public AppUser(UUID id, Long githubUserId,String login) {
        this.id = id;
        this.githubUserId = githubUserId;
        this.login = login;
    }

    public static AppUser build(User user) {

        return new AppUser(
                user.getId(),
                user.getGithubUserId(),
                user.getLogin());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public UUID getId() {
        return id;
    }

    public Long getGithubUserId() {
        return githubUserId;
    }

    public String getLogin() {
        return login;
    }
}
