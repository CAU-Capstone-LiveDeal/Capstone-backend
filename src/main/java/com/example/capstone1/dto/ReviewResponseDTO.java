package com.example.capstone1.dto;

import lombok.Data;

@Data
public class ReviewResponseDTO {
    private Long id;
    private String content;
    private int rating;
    private String authorUsername; // 작성자 이름
    private String storeName;      // 매장 이름
}