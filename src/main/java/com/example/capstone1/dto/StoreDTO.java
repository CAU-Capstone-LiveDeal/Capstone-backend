package com.example.capstone1.dto;

import lombok.Data;

@Data
public class StoreDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String category;
    private Double latitude;
    private Double longitude;
    private Integer totalTables;
    private Integer emptyTables;
    private Integer congestionLevel;
    private boolean discountActive;
    private Double averageRating;
}