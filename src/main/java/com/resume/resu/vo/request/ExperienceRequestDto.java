package com.resume.resu.vo.request;


import com.resume.resu.vo.response.ExperienceResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ExperienceRequestDto {
    private int exNo;
    private LocalDate exStartDate;
    private LocalDate exEndDate;
    private String exName;
    private String exDetail;
}
