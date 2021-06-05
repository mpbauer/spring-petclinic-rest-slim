package org.springframework.samples.petclinic.security;

import java.util.Set;

public final class User {
    private final String username;
    private final Set<String> roles;

    public User(String username, Set<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
