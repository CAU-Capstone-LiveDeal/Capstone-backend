package com.example.capstone1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateUsernameDTO {
    @Schema(description = "New username for the user", example = "newUsername123")
    private String newUsername;
}