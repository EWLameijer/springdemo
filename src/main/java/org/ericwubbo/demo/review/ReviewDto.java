package org.ericwubbo.demo.review;

public record ReviewDto(String username, int rating, String text) {
    public ReviewDto(Review review) {
        this(review.getUser().getUsername(), review.getRating(), review.getText());
    }
}
