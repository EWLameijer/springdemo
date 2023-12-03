package org.ericwubbo.demo.authority;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity(name = "authorities")
@NoArgsConstructor
@AllArgsConstructor
public class Authority {
    @Id
    private String username;

    private String authority;
}
