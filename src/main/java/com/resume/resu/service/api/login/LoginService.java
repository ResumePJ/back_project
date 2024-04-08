package com.resume.resu.service.api.login;

import com.resume.resu.vo.request.EmailLoginRequestDto;
import com.resume.resu.vo.response.MemberDTO;

public interface LoginService {
    MemberDTO checkForLogin(EmailLoginRequestDto emailLoginRequestDto);
}
