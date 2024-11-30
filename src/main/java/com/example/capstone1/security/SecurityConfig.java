package com.example.capstone1.security;

import com.example.capstone1.security.jwt.JwtAuthenticationFilter;
import com.example.capstone1.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

    public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                          UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
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

                        // 할인 조회는 모두 허용
                        .requestMatchers(HttpMethod.GET, "/api/discounts").permitAll()

                        // GET 요청에 대해 /api/orders 허용
                        .requestMatchers(HttpMethod.GET, "/api/orders", "/api/orders/{orderId}").permitAll()
                        // 주문 생성, 수정, 삭제는 인증 필요
                        .requestMatchers("/api/orders/**").authenticated()
                        // 사용자 정보 수정은 인증 필요
                        .requestMatchers("/api/users/**").authenticated()
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
        //configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}