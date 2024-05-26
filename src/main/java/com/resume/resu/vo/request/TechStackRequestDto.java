package com.resume.resu.vo.request;

import lombok.Data;

@Data
public class TechStackRequestDto {
    private int techNo;
    private byte type;
    private String techName;
    private byte techLevel;
}
