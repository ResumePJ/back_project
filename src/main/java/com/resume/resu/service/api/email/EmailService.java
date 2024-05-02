package com.resume.resu.service.api.email;


import com.resume.resu.vo.request.EmailAuthenticationRequestDto;

public interface EmailService {
    int send(String email,String subject, String text, int code);
    boolean authenticateEmail(EmailAuthenticationRequestDto emailAuthenticationRequestDto);
}
