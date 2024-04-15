package com.resume.resu.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResumeBasicInfoRequestDTO {
    private String name;
    private boolean gender;
    private String phone;
    private String address;
    private String intro;

    //구직 유형
    private boolean type;
}
