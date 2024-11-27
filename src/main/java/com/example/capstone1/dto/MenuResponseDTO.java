package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuResponseDTO {
    private Long id;
    private String name;
    private Double price;
    private String storeName;
    private boolean discountActive;
    private Double discountRate;
    private Double discountedPrice;
}