package org.ericwubbo.demo;

import org.ericwubbo.demo.movie.Movie;
import org.ericwubbo.demo.movie.MovieRepository;
import org.ericwubbo.demo.review.Review;
import org.ericwubbo.demo.review.ReviewRepository;
import org.ericwubbo.demo.user.User;
import org.ericwubbo.demo.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Seeder implements CommandLineRunner {
    private final MovieRepository movieRepository;

    private final UserRepository userRepository;

    private final ReviewRepository reviewRepository;

    public Seeder(MovieRepository movieRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (movieRepository.count() == 0) {
            var up = new Movie("Up");
            var citizenKane = new Movie("Citizen Kane");
            var theGrandBudapest = new Movie("The Grand Budapest Hotel");
            var starWars = new Movie("Star Wars");
            movieRepository.saveAll(List.of(up, citizenKane, theGrandBudapest));

            var me = new User("TheWub", "password123");
            var testUser = new User("nn", "abc");
            userRepository.saveAll(List.of(me, testUser));

            var myCitizenKaneReview = new Review(citizenKane, me, 2, "famous, but disappointing");
            var myUpReview = new Review(up, me, 5, "touching, surprising, and funny");
            var testGrandBudapestReview = new Review(theGrandBudapest, testUser, 3, "sometimes funny, but mostly mwuh");
            var testUpReview = new Review(up, testUser, 1, "I don't like cartoons");
            reviewRepository.saveAll(List.of(myCitizenKaneReview, myUpReview, testGrandBudapestReview, testUpReview));
        }
    }
}
