package com.example.capstone1.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Setter
    @Getter
    @Column(nullable = false)
    private String name; // 매장 이름

    @Setter
    @Getter
    @Column(nullable = false)
    private String address; // 매장 주소

    @Setter
    @Getter
    @Column(nullable = false)
    private String phone; // 매장 전화번호

    @Setter
    @Getter
    @Column(nullable = false)
    private String category; // 매장 카테고리 추가

    @Setter
    @Getter
    @Column(nullable = false)
    private Double latitude; // 위도 필드 추가

    @Setter
    @Getter
    @Column(nullable = false)
    private Double longitude; // 경도 필드 추가

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner; // 매장 소유자 필드 추가

    @Getter
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // 순환 참조 방지
    private List<Review> reviews; // 매장 리뷰 목록

    @Getter
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus; // 매장 메뉴 목록

    @Setter
    @Getter
    @Column(nullable = false)
    private Integer totalTables; // 총 테이블 수

    @Setter
    @Getter
    @Column(nullable = false)
    private Integer emptyTables; // 빈 테이블 수

    @Setter
    @Getter
    @Column(nullable = false)
    private Integer congestionLevel; // 혼잡도 (1~4)

    // 기본 생성자
    public Store() {
    }

    // 생성자
    public Store(String name, String address, String phone, String category, Double latitude, Double longitude, User owner, Integer totalTables, Integer emptyTables) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.owner = owner;
        this.totalTables = totalTables;
        this.emptyTables = emptyTables;
        this.congestionLevel = calculateCongestionLevel(); // 혼잡도 초기화
    }

    // 혼잡도 계산 메서드
    public Integer calculateCongestionLevel() {
        if (totalTables == 0) {
            return 0; // 테이블 정보가 없는 경우 혼잡도를 0으로 설정
        }
        double ratio = (double) emptyTables / totalTables; // 빈 테이블 비율 계산
        if (ratio <= 0.25) return 4;
        else if (ratio <= 0.5) return 3;
        else if (ratio <= 0.75) return 2;
        else return 1;
    }

    // 빈 테이블 감소 메서드
    public void decreaseEmptyTables() {
        if (this.emptyTables > 0) {
            this.emptyTables -= 1; // 빈 테이블 감소
            this.congestionLevel = calculateCongestionLevel(); // 혼잡도 업데이트
        } else {
            throw new IllegalStateException("No empty tables available for orders.");
        }
    }

    // 빈 테이블 증가 메서드 (필요시 추가)
    public void increaseEmptyTables() {
        if (this.emptyTables < this.totalTables) {
            this.emptyTables += 1; // 빈 테이블 증가
            this.congestionLevel = calculateCongestionLevel(); // 혼잡도 업데이트
        } else {
            throw new IllegalStateException("All tables are already empty.");
        }
    }
}