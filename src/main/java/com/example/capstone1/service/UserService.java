package com.example.capstone1.service;

import com.example.capstone1.dto.*;
import com.example.capstone1.model.User;
import com.example.capstone1.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails; // 추가됨
import org.springframework.security.core.userdetails.UserDetailsService; // 추가됨
import org.springframework.security.core.userdetails.UsernameNotFoundException; // 추가됨
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 생성자 주입 사용
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // UserDetailsService의 메서드 구현
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(() -> "ROLE_" + user.getRole())
        );
    }

    // 사용자 저장
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // 사용자 등록
    public User registerUser(UserRegisterDTO registerDTO, String role) {
        if (userRepository.findByUsername(registerDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken!");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setAddress(registerDTO.getAddress());
        user.setLatitude(registerDTO.getLatitude());
        user.setLongitude(registerDTO.getLongitude());
        user.setRole(role);

        return userRepository.save(user);
    }

    // 사용자 이름으로 조회
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // 사용자 선호도 업데이트
    public void updatePreferences(String username, UserPreferencesDTO preferencesDTO) {
        User user = findByUsername(username);
        user.setPreferences(preferencesDTO.getPreferences());
        userRepository.save(user);
    }

    // 사용자 이름 업데이트
    public void updateUsername(String currentUsername, UserUpdateUsernameDTO updateUsernameDTO) {
        User user = findByUsername(currentUsername);
        user.setUsername(updateUsernameDTO.getNewUsername());
        userRepository.save(user);
    }

    // 사용자 비밀번호 업데이트
    public void updatePassword(String username, UserUpdatePasswordDTO updatePasswordDTO) {
        User user = findByUsername(username);
        user.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    // 사용자 위치 업데이트
    public void updateLocation(String username, UserLocationDTO locationDTO) {
        User user = findByUsername(username);
        user.setAddress(locationDTO.getAddress());
        user.setLatitude(locationDTO.getLatitude());
        user.setLongitude(locationDTO.getLongitude());
        userRepository.save(user);
    }
}