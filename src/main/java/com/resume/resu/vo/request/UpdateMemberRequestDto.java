package com.resume.resu.vo.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UpdateMemberRequestDto {
    private String email; // 이메일
    private String password; // 비밀번호
    private String name; // 이름(닉네임)
    private String phone; // 전화번호

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth; // 생년월일

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate; // 가입 일자

    private int type; // 가입 유형
}
