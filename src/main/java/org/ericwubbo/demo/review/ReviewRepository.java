package org.ericwubbo.demo.review;

import org.ericwubbo.demo.movie.Movie;
import org.ericwubbo.demo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserAndMovie(User user, Movie movie);
}
