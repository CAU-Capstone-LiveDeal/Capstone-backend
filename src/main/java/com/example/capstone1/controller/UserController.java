package com.example.capstone1.controller;

import com.example.capstone1.dto.*;
import com.example.capstone1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 사용자 선호도 추가 및 수정
    @PutMapping("/preferences")
    public ResponseEntity<?> updatePreferences(@RequestBody UserPreferencesDTO preferencesDTO) {
        try {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            userService.updatePreferences(username, preferencesDTO);
            return ResponseEntity.ok("Preferences updated successfully for user: " + username);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 사용자 이름 수정
    @PutMapping("/username")
    public ResponseEntity<?> updateUsername(@RequestBody UserUpdateUsernameDTO updateUsernameDTO) {
        try {
            String currentUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            userService.updateUsername(currentUsername, updateUsernameDTO);
            return ResponseEntity.ok("Username updated successfully to: " + updateUsernameDTO.getNewUsername());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 사용자 비밀번호 수정
    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody UserUpdatePasswordDTO updatePasswordDTO) {
        try {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            userService.updatePassword(username, updatePasswordDTO);
            return ResponseEntity.ok("Password updated successfully for user: " + username);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 사용자 위치 수정
    @PutMapping("/location")
    public ResponseEntity<?> updateLocation(@RequestBody UserLocationDTO locationDTO) {
        try {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            userService.updateLocation(username, locationDTO);
            return ResponseEntity.ok("Location updated successfully for user: " + username);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}