package org.ericwubbo.demo;

import lombok.RequiredArgsConstructor;
import org.ericwubbo.demo.genre.Genre;
import org.ericwubbo.demo.genre.GenreRepository;
import org.ericwubbo.demo.movie.Movie;
import org.ericwubbo.demo.movie.MovieRepository;
import org.ericwubbo.demo.review.Review;
import org.ericwubbo.demo.review.ReviewRepository;
import org.ericwubbo.demo.user.MARole;
import org.ericwubbo.demo.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
    private final MovieRepository movieRepository;

    private final UserService userService;

    private final ReviewRepository reviewRepository;

    private final GenreRepository genreRepository;

    @Override
    public void run(String... args) throws Exception {
        if (movieRepository.count() == 0) {
            var adventure = new Genre("adventure");
            var animation = new Genre("animation");
            var comedy = new Genre("comedy");
            var crime = new Genre("crime");
            var drama = new Genre("drama");
            var family = new Genre("family");
            var mystery = new Genre("mystery");
            genreRepository.saveAll(List.of(adventure, animation, comedy, crime, drama, family, mystery));
            var up = new Movie("Up", Set.of(adventure, animation, comedy, drama, family));
            var citizenKane = new Movie("Citizen Kane", Set.of(drama, mystery));
            var theGrandBudapest = new Movie("The Grand Budapest Hotel", Set.of(adventure, comedy, crime));
            movieRepository.saveAll(List.of(up, citizenKane, theGrandBudapest));

            var me = userService.save("TheWub", "password123", MARole.ADMIN);
            var testUser = userService.save("nn", "abc", MARole.USER);

            var myCitizenKaneReview = new Review(citizenKane, me, 2, "famous, but disappointing");
            var myUpReview = new Review(up, me, 5, "touching, surprising, and funny");
            var testGrandBudapestReview = new Review(theGrandBudapest, testUser, 3, "sometimes funny, but mostly mwuh");
            var testUpReview = new Review(up, testUser, 1, "I don't like cartoons");
            reviewRepository.saveAll(List.of(myCitizenKaneReview, myUpReview, testGrandBudapestReview, testUpReview));
        }
    }
}
