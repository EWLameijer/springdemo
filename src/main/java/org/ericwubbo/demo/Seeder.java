package org.ericwubbo.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Seeder implements CommandLineRunner {
    private final MovieRepository movieRepository;

    public Seeder(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (movieRepository.count() == 0) {
            var seedMovies = List.of(
                    new Movie("Up", 5),
                    new Movie("Citizen Kane", 2),
                    new Movie("The Grand Budapest Hotel", 3)
                    );
            for (Movie movie : seedMovies) movieRepository.save(movie);
        }
    }
}
