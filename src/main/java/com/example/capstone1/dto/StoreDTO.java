package com.example.capstone1.dto;

import lombok.Data;

import java.util.List;

@Data
public class StoreDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String category;
    private Double latitude;
    private Double longitude;
    private List<ReviewDTO> reviews; // 리뷰 데이터는 DTO로 포함
}