package com.example.capstone1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 할인율 (0.0 ~ 1.0)
    @Column(nullable = false)
    private Double discountRate;

    // 할인 시작 시간
    @Column(nullable = false)
    private LocalDateTime startTime;

    // 할인 종료 시간
    @Column(nullable = false)
    private LocalDateTime endTime;

    // 해당 메뉴
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    // 할인 활성화 여부
    @Column(nullable = false)
    private boolean active = false;
}