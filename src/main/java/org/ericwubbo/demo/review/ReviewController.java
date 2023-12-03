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
        return reviewRepository.findById(id).map(review -> ResponseEntity.ok(ReviewDto.from(review)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
