package com.resume.resu.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ResumeBasicInfoDTO {
    private int resumeNo;
    private int memberNo;

    //tinyint(1) 타입 - 구직 유형
    private int type;

    private String name;

    /* TODO :  타입 수정 해야 할 지도? - tinyint(1) 타입*/
    private boolean gender;

    private LocalDate birth;

    private String phone;
    private String address;

    private String intro;
    private String resumeTitle;
    private boolean basic;


}
