package org.ericwubbo.demo.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.ericwubbo.demo.movie.Movie;
import org.ericwubbo.demo.user.User;

@Entity
public class Review {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonBackReference
    private Movie movie;

    private int rating;

    private String text;

    Review() {
    }

    public Review(Movie movie, User user, int rating, String text) {
        this.movie = movie;
        this.user = user;
        this.rating = rating;
        this.text = text;
    }

    public Movie getMovie() {
        return movie;
    }
    
    public User getUser() {
        return user;
    }

    public int getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }
}
