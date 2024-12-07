package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class TopFiveRecommendationRequestDTO {

    @NotNull(message = "preferType은 필수입니다.")
    @Size(min = 1, message = "preferType은 최소 1자 이상이어야 합니다.")
    private List<String> preferType; // 사용자의 중요도 (importance)

    @NotNull(message = "preferCategories는 필수입니다.")
    @Size(min = 1, message = "preferCategories는 최소 1자 이상이어야 합니다.")
    private String preferCategories; // 사용자의 선호도 (preferences)

    @NotNull(message = "storeList는 필수입니다.")
    @Size(min = 1, message = "storeList에는 최소 1개의 매장 정보가 포함되어야 합니다.")
    private List<StoreRecommendationDetailDTO> storelist; // 매장 목록

    public void setPreferType(List<String> preferType) {
        // 쉼표로 연결된 문자열을 개별 문자열 리스트로 변환
        this.preferType = preferType.stream()
                .flatMap(type -> Arrays.stream(type.split(",")))
                .map(String::trim) // 공백 제거
                .toList();
    }
}