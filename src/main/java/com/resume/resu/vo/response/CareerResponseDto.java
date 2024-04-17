package com.resume.resu.vo.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CareerResponseDto {
    private int carNo;
    private int resumeNo;
    private String company;
    private String dept;
    private String position;
    private LocalDate carStartDate;
    private LocalDate carEndDate;
    private String carDetail;
}
