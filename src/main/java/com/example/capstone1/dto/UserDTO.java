package com.example.capstone1.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String address;
    private String preferences;
    private String importance; // 중요성 추가
    private Double latitude;  // 경도 추가
    private Double longitude; // 위도 추가
    private String role;      // 역할 추가
}