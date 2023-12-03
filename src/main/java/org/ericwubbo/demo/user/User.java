package org.ericwubbo.demo.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @Getter
    private String username;

    private String password;

    private boolean enabled = true;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
