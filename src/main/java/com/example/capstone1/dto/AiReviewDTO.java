// Rename ReviewDTO to AiReviewDTO
package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiReviewDTO {
    private Long id;
    private String content;
    private int rating;
    private String authorUsername;
    private String storeName;
    private Long storeId;
}