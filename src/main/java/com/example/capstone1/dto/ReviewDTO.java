package com.example.capstone1.dto;

import lombok.Data;

@Data
public class ReviewDTO {
    private Long id;
    private String content;
    private int rating;
    private String authorUsername; // 작성자 정보
}