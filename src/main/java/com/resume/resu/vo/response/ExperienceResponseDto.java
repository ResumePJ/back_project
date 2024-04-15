package com.resume.resu.vo.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExperienceResponseDto {
    int exNo;
    int resumeNo;
    LocalDate exStartDate;
    LocalDate exEndDate;
    String exName;
    String exDetail;
}
