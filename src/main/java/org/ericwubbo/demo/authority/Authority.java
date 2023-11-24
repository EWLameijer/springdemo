package org.ericwubbo.demo.authority;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "authorities")
public class Authority {
    @Id
    private String username;

    private String authority;

    Authority() {
    }

    public Authority(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }
}
