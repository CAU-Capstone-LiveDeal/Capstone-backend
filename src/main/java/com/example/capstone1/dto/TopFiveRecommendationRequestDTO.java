package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class TopFiveRecommendationRequestDTO {

    @NotNull(message = "preferType은 필수입니다.")
    @Size(min = 1, message = "preferType은 최소 1자 이상이어야 합니다.")
    private String preferType; // 사용자의 중요도 (importance)

    @NotNull(message = "preferCategories는 필수입니다.")
    @Size(min = 1, message = "preferCategories는 최소 1자 이상이어야 합니다.")
    private String preferCategories; // 사용자의 선호도 (preferences)

    @NotNull(message = "storeList는 필수입니다.")
    @Size(min = 1, message = "storeList에는 최소 1개의 매장 정보가 포함되어야 합니다.")
    private List<StoreRecommendationDetailDTO> storelist; // 매장 목록
}