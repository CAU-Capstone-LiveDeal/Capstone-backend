package com.example.capstone1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdatePasswordDTO {
    @Schema(description = "New password for the user", example = "newPassword123")
    private String newPassword;
}