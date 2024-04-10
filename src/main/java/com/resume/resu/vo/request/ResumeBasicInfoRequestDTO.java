package com.resume.resu.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResumeBasicInfoRequestDTO {
    public String name;
    public boolean gender;
    public String phone;
    public String address;
    public String intro;

    //구직 유형
    public boolean type;
}
