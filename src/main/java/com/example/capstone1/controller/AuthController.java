package com.example.capstone1.controller;

import com.example.capstone1.dto.JwtResponse;
import com.example.capstone1.dto.UserLoginDTO;
import com.example.capstone1.dto.UserRegisterDTO;
import com.example.capstone1.service.UserService;
import com.example.capstone1.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // JWT 토큰 생성용

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDTO registerDTO) {
        try {
            userService.registerUser(registerDTO, "USER"); // 기본 역할 설정
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody UserRegisterDTO registerDTO) {
        try {
            userService.registerUser(registerDTO, "ADMIN"); // 관리자 역할 설정
            return ResponseEntity.ok("Admin registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserLoginDTO loginDTO) {
        try {
            // 사용자 인증
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 권한 정보 가져오기
            String username = authentication.getName();
            var roles = authentication.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .collect(Collectors.toList());

            // JWT 생성
            String token = jwtTokenProvider.generateToken(username, roles);

            // JwtResponse로 응답
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}