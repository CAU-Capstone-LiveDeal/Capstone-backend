package com.example.capstone1.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String content; // 리뷰 내용

    @Getter
    @Setter
    @Column(nullable = false)
    private int rating; // 평점 (1~5)

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author; // 작성자

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    @JsonBackReference // 순환 참조 방지
    private Store store; // 매장

    // 기본 생성자
    public Review() {}

    // 생성자
    public Review(String content, int rating, User author, Store store) {
        this.content = content;
        this.rating = rating;
        this.author = author;
        this.store = store;
    }
}