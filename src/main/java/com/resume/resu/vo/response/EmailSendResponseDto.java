package com.resume.resu.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailSendResponseDto {
    private String email;
    private String message;

    public EmailSendResponseDto(String email){
        this.email=email;
    }
}
