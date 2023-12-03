package org.ericwubbo.demo.user;

public enum MARole {
    ADMIN, USER;

    public String asAuthority() {
        return "ROLE_" + name();
    }

    public String asRole() {
        return name();
    }
}
