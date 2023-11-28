package org.ericwubbo.demo.user;

public enum MARole {
    ADMIN, USER;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
