package org.ericwubbo.demo.movie;

import org.ericwubbo.demo.review.ReviewDto;

import java.util.List;
import java.util.OptionalDouble;

public record MovieDto(Long id, String title, List<ReviewDto> reviews) {
    public MovieDto(Movie movie) {
        this(movie.getId(), movie.getTitle(), movie.getReviews().stream().map(ReviewDto::new).toList());
    }

    public OptionalDouble getAverageRating() {
        return reviews.stream().mapToDouble(ReviewDto::rating).average();
    }
}
