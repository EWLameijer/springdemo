package org.ericwubbo.demo.genre;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Genre {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Genre(String name) {
        this.name = name;
    }
}
