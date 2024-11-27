package com.example.capstone1.controller;

import com.example.capstone1.dto.*;
import com.example.capstone1.model.User;
import com.example.capstone1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 사용자 선호도 수정
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

    // 사용자 정보 조회
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(userService.mapToUserDTO(user));
    }

    // 모든 사용자 조회
    @GetMapping("/")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.findAllUsers();
            List<UserDTO> userDTOs = users.stream()
                    .map(userService::mapToUserDTO)
                    .toList();
            return ResponseEntity.ok(userDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("사용자 목록을 가져오는 중 오류가 발생했습니다: " + e.getMessage());
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

    // 중요성 조회
    @GetMapping("/importance")
    public ResponseEntity<?> getImportance(Authentication authentication) {
        try {
            String username = authentication.getName();
            String importance = userService.getImportance(username);
            return ResponseEntity.ok("Importance: " + importance);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching importance: " + e.getMessage());
        }
    }

    // 중요성 수정
    @PutMapping("/importance")
    public ResponseEntity<?> updateImportance(@RequestBody UserImportanceDTO importanceDTO, Authentication authentication) {
        try {
            String username = authentication.getName();
            userService.updateImportance(username, importanceDTO);
            return ResponseEntity.ok("Importance updated successfully to: " + importanceDTO.getImportance());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating importance: " + e.getMessage());
        }
    }
}