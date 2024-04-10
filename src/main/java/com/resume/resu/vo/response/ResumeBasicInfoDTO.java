package com.resume.resu.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ResumeBasicInfoDTO {
    public int resumeNo;
    public int memberNo;

    //tinyint(1) 타입 - 구직 유형
    public int type;

    public String name;

    /* TODO :  타입 수정 해야 할 지도? - tinyint(1) 타입*/
    public boolean gender;

    public LocalDate birth;

    public String phone;
    public String address;
//    public String photo;
    public String intro;
    public String resumeTitle;
    public boolean basic;

}
