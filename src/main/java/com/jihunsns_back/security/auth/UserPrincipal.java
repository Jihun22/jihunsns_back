package com.jihunsns_back.security.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class UserPrincipal  implements Authentication {
    private final Long id;
    private final String role;
    private boolean authenticated = true;

    public UserPrincipal(Long id, String role) {
        this.id = id;
        this.role = role;
    }
    public Long getId() {return id;}

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }
    @Override public Object getCredentials() {return null;}
    @Override public Object getDetails() {return null;}
    @Override public Object getPrincipal() {return id;}
    @Override public boolean isAuthenticated() {return authenticated;}
    @Override public void setAuthenticated(boolean isAuthenticated) {this.authenticated = isAuthenticated;}
    @Override public String getName() {return null;}
}
