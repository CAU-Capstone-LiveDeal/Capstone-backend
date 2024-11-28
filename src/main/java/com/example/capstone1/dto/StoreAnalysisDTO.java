package com.example.capstone1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreAnalysisDTO {
    private Long storeId;
    private int taste;
    private int service;
    private int interior;
    private int cleanliness;
}
