package com.example.capstone1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 메뉴 이름
    @Column(nullable = false)
    private String name;

    // 메뉴 가격
    @Column(nullable = false)
    private Double price;

    // 매장 정보와 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    // 할인 상태
    @Column(nullable = false)
    private boolean discountActive = false;

    // 할인율 (0.0 ~ 1.0)
    @Column
    private Double discountRate;

    // 할인된 금액
    @Column
    private Double discountedPrice;

    // 메뉴별 할인 목록
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Discount> discounts;

    // 기본 생성자
    public Menu() {
    }

    // 생성자
    public Menu(Store store, String name, Double price) {
        this.store = store;
        this.name = name;
        this.price = price;
    }

    // 할인 적용 메서드
    public void applyDiscount(double discountRate) {
        this.discountActive = true;
        this.discountRate = discountRate;
        this.discountedPrice = (double) (Math.round(this.price * (1 - discountRate) / 100) * 100);
    }

    // 할인 해제 메서드
    public void removeDiscount() {
        this.discountActive = false;
        this.discountRate = null;
        this.discountedPrice = null;
    }
}