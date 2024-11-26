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
    private Integer totalTables; // 총 테이블 수
    private Integer emptyTables; // 빈 테이블 수
    private Integer congestionLevel; // 혼잡도
}