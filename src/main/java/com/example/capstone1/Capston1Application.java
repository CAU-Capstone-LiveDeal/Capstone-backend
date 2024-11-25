package com.example.capstone1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 스케줄링 기능 활성화
public class Capston1Application {

    public static void main(String[] args) {
        SpringApplication.run(Capston1Application.class, args);
    }

}