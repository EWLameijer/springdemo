package org.ericwubbo.demo.movie;

import lombok.RequiredArgsConstructor;
import org.ericwubbo.demo.BadInputException;
import org.ericwubbo.demo.NotFoundException;
import org.ericwubbo.demo.review.Review;
import org.ericwubbo.demo.review.ReviewDto;
import org.ericwubbo.demo.review.ReviewRepository;
import org.ericwubbo.demo.user.User;
import org.ericwubbo.demo.user.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieRepository movieRepository;

    private final ReviewRepository reviewRepository;

    private final UserService userService;

    private final static String TITLE_CANNOT_BE_BLANK = "A movie requires a (non-blank) title";

    @GetMapping("{id}")
    public ResponseEntity<MovieDto> getById(@PathVariable long id) {
        Optional<Movie> possibleMovie = movieRepository.findById(id);
        return possibleMovie.map(movie -> ResponseEntity.ok(MovieDto.from(movie)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("reviews/{id}")
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable long id) {
        Optional<Movie> possibleMovie = movieRepository.findById(id);
        if (possibleMovie.isEmpty()) return ResponseEntity.notFound().build();
        var movieReviews = possibleMovie.get().getReviews();
        return ResponseEntity.ok(movieReviews.stream().map(ReviewDto::from).toList());
    }

    @GetMapping
    public Iterable<MovieDto> getAll(Pageable pageable) {
        return movieRepository.findAll(
                        PageRequest.of(
                                pageable.getPageNumber(),
                                Math.min(pageable.getPageSize(), 3),
                                pageable.getSortOr(Sort.by("title"))))
                .map(MovieDto::from);
    }

    @PostMapping
    public ResponseEntity<MovieDto> add(@RequestBody MovieDto movieDto, UriComponentsBuilder ucb) {
        var title = getTitleOrThrowErrorOnBadInput(movieDto);
        verifyThatMovieDoesNotYetExist(title);
        var newMovie = new Movie(title, Set.of());
        movieRepository.save(newMovie);
        URI locationOfNewMovie = ucb.path("movies/{id}").buildAndExpand(newMovie.getId()).toUri();
        return ResponseEntity.created(locationOfNewMovie).body(MovieDto.from(newMovie));
    }

    private void verifyThatMovieDoesNotYetExist(String title) {
        var possibleExistingMovie = movieRepository.findByTitleIgnoringCase(title);
        if (possibleExistingMovie.isPresent()) throw new BadInputException("Movie already exists");
    }

    private static String getTitleOrThrowErrorOnBadInput(MovieDto movieDto) {
        if (movieDto.id() != null)
            throw new BadInputException("the body of this POST request should not contain an id value, as that is assigned by the database");
        if (movieDto.reviews() != null) throw new BadInputException("A movie should be created without reviews");
        var rawTitle = movieDto.title();
        if (rawTitle == null || rawTitle.isBlank()) throw new BadInputException(TITLE_CANNOT_BE_BLANK);
        return rawTitle.trim();
    }

    private static void checkBodyId(Movie movie, long id) {
        var idFromBody = movie.getId();
        if (idFromBody != null && idFromBody != id)
            throw new BadInputException("ids given by path and body are inconsistent, and the id of an item should not be changed");
    }

    @PatchMapping("{id}")
    public ResponseEntity<MovieDto> patch(@RequestBody Movie changedMovie, @PathVariable long id) {
        checkBodyId(changedMovie, id);
        var possibleOriginalMovie = movieRepository.findById(id);
        if (possibleOriginalMovie.isEmpty()) return ResponseEntity.notFound().build();

        var movie = possibleOriginalMovie.get();
        var newTitle = changedMovie.getTitle();
        if (newTitle == null)
            throw new BadInputException("PATCH body does not contain any values that can change the movie.");

        if (newTitle.isBlank()) throw new BadInputException(TITLE_CANNOT_BE_BLANK);
        var title = newTitle.trim();
        verifyThatMovieDoesNotYetExist(title);
        movie.setTitle(title);

        movieRepository.save(movie);
        return ResponseEntity.ok(MovieDto.from(movie));
    }

    @GetMapping("search/titles/{query}")
    public ResponseEntity<Iterable<MovieDto>> findTitlesContaining(@PathVariable String query) {
        var movieDtos = movieRepository.findByTitleIgnoringCaseContaining(query).stream().map(MovieDto::from).toList();
        return ResponseEntity.ok(movieDtos);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        if (movieRepository.findById(id).isPresent()) {
            movieRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.notFound().build();
    }

    @PostMapping("{movieId}/reviews")
    public ResponseEntity<ReviewDto> postReview(@RequestBody ReviewDto review,
                                                @PathVariable long movieId, Principal principal, UriComponentsBuilder ucb) {
        var movie = getMovie(movieId);
        var user = getUser(principal);
        var possiblyExistingReview = reviewRepository.findByUserAndMovie(user, movie);
        if (possiblyExistingReview.isPresent())
            throw new BadInputException("This user has already written a review for this movie.");
        checkRating(review);
        var completeReview = new Review(movie, user, review.rating(), review.text());
        reviewRepository.save(completeReview);
        URI locationOfNewReview = ucb.path("reviews/{id}").buildAndExpand(completeReview.getId()).toUri();
        return ResponseEntity.created(locationOfNewReview).body(ReviewDto.from(completeReview));
    }

    @PatchMapping("{movieId}/reviews")
    public ResponseEntity<ReviewDto> changeReview(@RequestBody ReviewDto newReviewDto, @PathVariable long movieId,
                                                  Principal principal) {
        var movie = getMovie(movieId);
        var user = getUser(principal);
        var possiblyExistingReview = reviewRepository.findByUserAndMovie(user, movie);
        if (possiblyExistingReview.isEmpty())
            throw new BadInputException("This user has not yet written a review for this movie.");
        var currentReview = possiblyExistingReview.get();
        var rating = newReviewDto.rating();
        if (rating != null) {
            checkRating(newReviewDto);
            currentReview.setRating(rating);
        }
        var text = newReviewDto.text();
        if (text != null) currentReview.setText(text);
        reviewRepository.save(currentReview);
        return ResponseEntity.ok(ReviewDto.from(currentReview));
    }

    private static void checkRating(ReviewDto reviewDto) {
        var rating = reviewDto.rating();
        if (rating == null) throw new BadInputException("Each review should have a rating");
        if (rating > 5 || rating < 1) throw new BadInputException("Rating should be at least 1 and at most 5!");
    }

    private User getUser(Principal principal) {
        var possibleUser = userService.findByUsername(principal.getName());
        if (possibleUser.isEmpty()) throw new IllegalArgumentException("Error with principal!");
        return possibleUser.get();
    }

    private Movie getMovie(long movieId) {
        return movieRepository.findById(movieId).orElseThrow(NotFoundException::new);
    }
}
