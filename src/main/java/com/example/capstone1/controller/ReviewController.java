package com.example.capstone1.controller;

import com.example.capstone1.dto.ReviewRequestDTO;
import com.example.capstone1.dto.ReviewResponseDTO;
import com.example.capstone1.model.Store;
import com.example.capstone1.model.User;
import com.example.capstone1.service.ReviewService;
import com.example.capstone1.service.StoreService;
import com.example.capstone1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreService storeService;

    @PostMapping("/{storeId}")
    public ResponseEntity<?> addReview(@PathVariable Long storeId, @RequestBody ReviewRequestDTO reviewRequestDTO) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;

            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else if (principal instanceof String) {
                username = (String) principal;
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authentication principal");
            }

            User currentUser = userService.findByUsername(username);

            Store store = storeService.findById(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found"));

            ReviewResponseDTO responseDTO = reviewService.saveReview(reviewRequestDTO, currentUser, store);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable Long reviewId, @RequestBody ReviewRequestDTO updatedReviewDTO) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;

            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else if (principal instanceof String) {
                username = (String) principal;
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authentication principal");
            }

            User currentUser = userService.findByUsername(username);

            ReviewResponseDTO responseDTO = reviewService.updateReview(reviewId, updatedReviewDTO, currentUser);
            return ResponseEntity.ok(responseDTO);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<?> getReviewsByStore(@PathVariable Long storeId) {
        Store store = storeService.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found"));

        List<ReviewResponseDTO> reviews = reviewService.findByStore(store);
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;

            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else if (principal instanceof String) {
                username = (String) principal;
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authentication principal");
            }

            User currentUser = userService.findByUsername(username);

            reviewService.deleteReview(reviewId, currentUser);
            return ResponseEntity.ok("Review deleted successfully");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}