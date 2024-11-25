package com.example.capstone1.security;

import com.example.capstone1.security.jwt.JwtAuthenticationFilter;
import com.example.capstone1.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        // Swagger 관련 경로는 인증 없이 허용
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // 로그인 및 회원가입 경로는 인증 없이 허용
                        .requestMatchers(
                                "/api/login",
                                "/api/register",
                                "/api/register/admin"
                        ).permitAll()
                        // 조회 관련 경로는 인증 없이 허용
                        .requestMatchers(
                                "/api/menus/store/**",           // 특정 매장 메뉴 조회
                                "/api/reviews",                 // 전체 리뷰 조회
                                "/api/reviews/store/{storeId}", // 특정 매장 리뷰 조회
                                "/api/menus",                   // 전체 메뉴 조회
                                "/api/menus/{menuId}",          // 특정 메뉴 조회
                                "/api/stores/{storeId}/menus",  // 특정 매장의 모든 메뉴 조회
                                "/api/discounts",               // 전체 할인 조회
                                "/api/discounts/{discountId}",  // 특정 할인 조회
                                "/api/menus/{menuId}/discounts",// 특정 메뉴의 모든 할인 조회
                                "/api/orders",                  // 전체 주문 조회
                                "/api/orders/{orderId}",        // 특정 주문 조회
                                "/api/stores/{storeId}/orders"  // 특정 매장의 모든 주문 조회
                        ).permitAll()
                        // 사용자 정보 수정은 인증 필요
                        .requestMatchers(
                                "/api/users/**" // 사용자 선호도, 이름, 비밀번호, 위치 수정
                        ).authenticated()
                        // 메뉴 주문 생성, 수정, 삭제는 인증 필요
                        .requestMatchers("/api/orders/**").authenticated()
                        // 할인 등록, 수정, 삭제는 인증 필요
                        .requestMatchers("/api/discounts/**").authenticated()
                        // 메뉴 등록, 수정, 삭제는 인증 필요
                        .requestMatchers("/api/menus/**").authenticated()
                        // 나머지 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                // JWT 인증 필터 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                // CORS 설정 추가
                .cors().configurationSource(corsConfigurationSource());

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // 필요한 도메인으로 제한 가능
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}