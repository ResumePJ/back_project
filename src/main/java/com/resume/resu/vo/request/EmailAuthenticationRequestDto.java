package com.resume.resu.vo.request;

import lombok.Data;

@Data
public class EmailAuthenticationRequestDto {
    private String name;
    private String birth;
    private String email;
    private String code;
}
