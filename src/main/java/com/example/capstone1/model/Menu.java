package com.example.capstone1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Setter
    @Getter
    @Column(nullable = false)
    private String name; // 메뉴 이름

    @Setter
    @Getter
    @Column(nullable = false)
    private Double price; // 메뉴 가격

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; // 매장 정보와 연결

    @Getter
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Discount> discounts; // 메뉴별 할인 목록

    // 기본 생성자
    public Menu() {}

    // 필요한 생성자 추가
    public Menu(Store store, String name, Double price) {
        this.store = store;
        this.name = name;
        this.price = price;
    }
}