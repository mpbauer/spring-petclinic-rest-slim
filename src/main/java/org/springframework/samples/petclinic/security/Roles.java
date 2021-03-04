package org.springframework.samples.petclinic.security;

import org.springframework.stereotype.Component;

@Component
public class Roles {

    public static final String OWNER_ADMIN = "ROLE_OWNER_ADMIN";
    public static final String VET_ADMIN = "ROLE_VET_ADMIN";
    public static final String ADMIN = "ROLE_ADMIN";
}
