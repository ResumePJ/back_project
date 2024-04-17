package com.resume.resu.vo.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CareerRequestDto {
    private int carNo;
    private String company;
    private String dept;
    private String position;
    private LocalDate carStartDate;
    private LocalDate carEndDate;
    private String carDetail;
}
