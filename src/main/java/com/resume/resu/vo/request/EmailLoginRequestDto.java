package com.resume.resu.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class EmailLoginRequestDto {
    private String email;
    private String password;

}
