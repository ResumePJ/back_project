package com.resume.resu.service.impl;

import com.resume.resu.repository.LoginMapper;
import com.resume.resu.service.api.LoginService;
import com.resume.resu.vo.request.EmailLoginRequestDto;
import com.resume.resu.vo.response.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {

    public final LoginMapper loginMapper;
    @Override
    public MemberDTO checkForLogin(EmailLoginRequestDto emailLoginRequestDto) {
        MemberDTO user = loginMapper.findMemberInfoByEmail(emailLoginRequestDto);

        if(user !=null){
            log.info("checkForLogin is successful");
            return user;
        }else{
            log.info("checkForLogin is unsuccessful");
            return null;
        }

    }
}
