package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalysisScoreDTO {
    private Long storeId;
    private int ratingscore;
    private int taste;
    private int service;
    private int interior;
    private int cleanliness;
}