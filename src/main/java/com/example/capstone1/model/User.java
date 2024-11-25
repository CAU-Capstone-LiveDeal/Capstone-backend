package com.example.capstone1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Setter
    @Getter
    @Column(nullable = false, unique = true)
    private String username;

    @Setter
    @Getter
    @JsonIgnore // 비밀번호는 JSON 직렬화에서 제외
    @Column(nullable = false)
    private String password;

    @Setter
    @Getter
    @Column(nullable = false)
    private String role;

    @Setter
    @Getter
    @Column(nullable = false)
    private String address;

    @Setter
    @Getter
    @Column(nullable = false)
    private Double latitude;

    @Setter
    @Getter
    @Column(nullable = false)
    private Double longitude;

    @Setter
    @Getter
    @Column
    private String preferences; // 사용자 선호도 필드 추가

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // 순환 참조 방지
    @Getter
    private List<Review> reviews; // 사용자가 작성한 리뷰 목록

    // 기본 생성자
    public User() {}

    // 생성자
    public User(String username, String password, String role, String address, Double latitude, Double longitude) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}