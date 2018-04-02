package org.maksim.training.mtapp.entity;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    REGISTERED_USER, BOOKING_MANAGER;

    private final String authorityName = name().toUpperCase();
    private final String authority = "ROLE_" + authorityName;

    public String getAuthorityName() {
        return authorityName;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}