package com.example.capstone1.dto;

import lombok.Data;

@Data
public class StoreRequestDTO {
    private String name; // 매장 이름
    private String address; // 매장 주소
    private String phone; // 매장 전화번호
    private String category; // 매장 카테고리
    private Double latitude; // 위도
    private Double longitude; // 경도
    private Integer totalTables; // 총 테이블 수
    private Integer emptyTables; // 빈 테이블 수
}