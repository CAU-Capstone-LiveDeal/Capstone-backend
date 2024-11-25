package com.example.capstone1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Setter
    @Getter
    @Column(nullable = false)
    private Double percentage; // 할인율 (0 ~ 100)

    @Setter
    @Getter
    @Column(nullable = false)
    private LocalTime startTime; // 할인 시작 시간

    @Setter
    @Getter
    @Column(nullable = false)
    private LocalTime endTime; // 할인 종료 시간

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu; // 연결된 메뉴

    // 기본 생성자
    public Discount() {}

    // 필요한 생성자 추가
    public Discount(Menu menu, Double percentage, LocalTime startTime, LocalTime endTime) {
        this.menu = menu;
        this.percentage = percentage;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}