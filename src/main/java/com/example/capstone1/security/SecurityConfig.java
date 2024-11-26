package com.example.capstone1.security;

import com.example.capstone1.security.jwt.JwtAuthenticationFilter;
import com.example.capstone1.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    // 생성자 주입 사용
    public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                          UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    // AuthenticationManager를 빈으로 등록하고 UserDetailsService와 PasswordEncoder를 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
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
                        // 리뷰 등록, 수정, 삭제는 인증 필요
                        .requestMatchers("/api/reviews/**").authenticated()
                        // 나머지 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class)
                .cors().configurationSource(corsConfigurationSource());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // 필요한 도메인으로 제한 가능
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}