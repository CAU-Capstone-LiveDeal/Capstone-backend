package com.example.capstone1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPreferencesDTO {
    @Schema(description = "User preferences", example = "Vegetarian, Non-smoker")
    private String preferences;
}