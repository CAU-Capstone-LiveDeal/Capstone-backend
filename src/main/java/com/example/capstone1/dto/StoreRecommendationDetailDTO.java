package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class StoreRecommendationDetailDTO {

    @NotNull(message = "storeId는 필수입니다.")
    private Long storeId; // 매장 ID

    @NotNull(message = "storeType은 필수입니다.")
    private String storeType; // 매장 카테고리 (Store 엔티티의 category 필드와 매핑)

    @NotNull(message = "ratingscore는 필수입니다.")
    private Integer ratingscore; // 평점

    @NotNull(message = "taste는 필수입니다.")
    private Integer taste; // 맛 점수

    @NotNull(message = "service는 필수입니다.")
    private Integer service; // 서비스 점수

    @NotNull(message = "interior는 필수입니다.")
    private Integer interior; // 인테리어 점수

    @NotNull(message = "cleanliness는 필수입니다.")
    private Integer cleanliness; // 청결 점수
}