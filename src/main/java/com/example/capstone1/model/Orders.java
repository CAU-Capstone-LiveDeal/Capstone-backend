package com.example.capstone1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; // 매장 정보

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu; // 주문한 메뉴 정보

    @Setter
    @Getter
    @Column(nullable = false)
    private Integer quantity; // 주문 수량

    @Setter
    @Getter
    @Column(nullable = false)
    private LocalDateTime orderTime; // 주문 발생 시각

    @Setter
    @Getter
    @Column(nullable = false)
    private LocalDateTime expireTime; // 주문 만료 시각

    @Setter
    @Getter
    @Column(nullable = false)
    private boolean expired = false; // 주문 만료 여부
}