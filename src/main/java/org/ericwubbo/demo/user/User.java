package org.ericwubbo.demo.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "users")
public class User {
    @Id
    private String username;

    private String password;

    private boolean enabled;

    User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        enabled = true;
    }

    public String getUsername() {
        return username;
    }
}
