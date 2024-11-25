package com.example.capstone1.service;

import com.example.capstone1.dto.ReviewRequestDTO;
import com.example.capstone1.dto.ReviewResponseDTO;
import com.example.capstone1.model.Review;
import com.example.capstone1.model.Store;
import com.example.capstone1.model.User;
import com.example.capstone1.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public ReviewResponseDTO saveReview(ReviewRequestDTO reviewDTO, User author, Store store) {
        Review review = new Review(reviewDTO.getContent(), reviewDTO.getRating(), author, store);
        Review savedReview = reviewRepository.save(review);
        return mapToResponseDTO(savedReview);
    }

    public ReviewResponseDTO updateReview(Long reviewId, ReviewRequestDTO updatedReviewDTO, User currentUser) {
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        if (!existingReview.getAuthor().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not authorized to update this review");
        }

        existingReview.setContent(updatedReviewDTO.getContent());
        existingReview.setRating(updatedReviewDTO.getRating());
        Review updatedReview = reviewRepository.save(existingReview);

        return mapToResponseDTO(updatedReview);
    }

    public void deleteReview(Long reviewId, User currentUser) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        if (!review.getAuthor().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not authorized to delete this review");
        }

        reviewRepository.delete(review);
    }

    public List<ReviewResponseDTO> findByStore(Store store) {
        List<Review> reviews = reviewRepository.findByStore(store);
        return reviews.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    private ReviewResponseDTO mapToResponseDTO(Review review) {
        ReviewResponseDTO responseDTO = new ReviewResponseDTO();
        responseDTO.setId(review.getId());
        responseDTO.setContent(review.getContent());
        responseDTO.setRating(review.getRating());
        responseDTO.setAuthorUsername(review.getAuthor().getUsername());
        responseDTO.setStoreName(review.getStore().getName());
        return responseDTO;
    }
}