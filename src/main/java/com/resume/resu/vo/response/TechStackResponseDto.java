package com.resume.resu.vo.response;

import lombok.Data;

@Data
public class TechStackResponseDto {
    private int techNo;
    private int resumeNo;
    private byte type;
    private String techName;
    private byte techLevel;
}
