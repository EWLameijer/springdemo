package org.ericwubbo.demo.movie;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ericwubbo.demo.review.Review;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue
    private Long id;

    @Setter
    private String title;

    @OneToMany(mappedBy = "movie")
    private final Set<Review> reviews = new HashSet<>();

    public Movie(String title) {
        this.title = title;
    }
}
