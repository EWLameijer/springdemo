package org.ericwubbo.demo.review;

public record ReviewDto(String username, Integer rating, String text) {
    public static ReviewDto from(Review review) {
        return new ReviewDto(review.getUser().getUsername(), review.getRating(), review.getText());
    }
}
