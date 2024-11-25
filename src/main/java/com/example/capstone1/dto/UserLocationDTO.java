package com.example.capstone1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLocationDTO {
    @Schema(description = "New address for the user", example = "123 Main Street")
    private String address;

    @Schema(description = "Latitude of the new location", example = "37.7749")
    private Double latitude;

    @Schema(description = "Longitude of the new location", example = "-122.4194")
    private Double longitude;
}