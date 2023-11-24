package org.ericwubbo.demo.movie;

import org.ericwubbo.demo.BadInputException;
import org.ericwubbo.demo.review.ReviewDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("movies")
public class MovieController {
    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        Optional<Movie> possibleMovie = movieRepository.findById(id);
        if (possibleMovie.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new MovieDto(possibleMovie.get()));
    }

    @GetMapping("reviews/{id}")
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable long id) {
        Optional<Movie> possibleMovie = movieRepository.findById(id);
        if (possibleMovie.isEmpty()) return ResponseEntity.notFound().build();
        var movie = possibleMovie.get();
        var movieReviews = movie.getReviews();
        return ResponseEntity.ok(movieReviews.stream().map(ReviewDto::new).toList());
    }

    @GetMapping
    public Iterable<MovieDto> getAll(Pageable pageable) {
        return movieRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 3),
                pageable.getSortOr(Sort.by("title"))
        )).map(MovieDto::new);
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Movie movie, UriComponentsBuilder ucb) {
        if (movie.getId() != null) {
            var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                    "the body of this POST request should not contain an id value, as that is assigned by the database");
            return ResponseEntity.badRequest().body(problemDetail);
        }
        movieRepository.save(movie);
        URI locationOfNewMovie = ucb
                .path("movies/{id}")
                .buildAndExpand(movie.getId())
                .toUri();
        return ResponseEntity.created(locationOfNewMovie).body(movie);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> replace(@RequestBody Movie movie, @PathVariable long id) {
        checkBodyId(movie, id);
        var possibleOriginalMovie = movieRepository.findById(id);
        if (possibleOriginalMovie.isEmpty()) return ResponseEntity.notFound().build();
        movie.setId(id);
        movieRepository.save(movie);
        return ResponseEntity.noContent().build();
    }

    private static void checkBodyId(Movie movie, long id) {
        var idFromBody = movie.getId();
        if (idFromBody != null && idFromBody != id)
            throw new BadInputException("ids given by path and body are inconsistent, and the id of an item should not be changed");
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> patch(@RequestBody Movie changedMovie, @PathVariable long id) {
        checkBodyId(changedMovie, id);
        var possibleOriginalMovie = movieRepository.findById(id);
        if (possibleOriginalMovie.isEmpty()) return ResponseEntity.notFound().build();

        var movie = possibleOriginalMovie.get();
        var newTitle = changedMovie.getTitle();
        if (newTitle != null) movie.setTitle(newTitle);

        movieRepository.save(movie);
        return ResponseEntity.ok(movie);
    }

    @GetMapping("search/titles/{query}")
    public ResponseEntity<Iterable<MovieDto>> findTitlesContaining(@PathVariable String query) {
        List<MovieDto> movieDtos = movieRepository.findByTitleIgnoringCaseContaining(query)
                .stream().map(MovieDto::new).toList();
        return ResponseEntity.ok(movieDtos);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        if (movieRepository.findById(id).isPresent()) {
            movieRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.notFound().build();
    }
}
