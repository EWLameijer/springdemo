package org.ericwubbo.demo.review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewRepository reviewRepository;

    @GetMapping("{id}")
    public ResponseEntity<ReviewDto> get(@PathVariable long id) {
        var possibleReview = reviewRepository.findById(id);
        return possibleReview.map(review -> ResponseEntity.ok(new ReviewDto(review)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
