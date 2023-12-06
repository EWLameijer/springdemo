package org.ericwubbo.demo.movie;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ericwubbo.demo.genre.Genre;
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

    @ManyToMany
    private Set<Genre> genres = new HashSet<>();

    public Movie(String title, Set<Genre> genres) {
        this.title = title;
        this.genres = genres;
    }
}
