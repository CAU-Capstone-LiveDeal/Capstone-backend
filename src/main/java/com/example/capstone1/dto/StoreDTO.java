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

    // Getter에서 소수점 제한
    public Double getLatitude() {
        return latitude != null ? Math.round(latitude * 1_000_000) / 1_000_000.0 : null;
    }

    public Double getLongitude() {
        return longitude != null ? Math.round(longitude * 1_000_000) / 1_000_000.0 : null;
    }

    public Double getAverageRating() {
        return averageRating != null ? Math.round(averageRating * 100) / 100.0 : null;
    }
}