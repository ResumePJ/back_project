package com.resume.resu.controller.login;

import com.resume.resu.service.api.login.LoginService;
import com.resume.resu.util.JwtUtils;
import com.resume.resu.vo.request.EmailLoginRequestDto;
import com.resume.resu.vo.response.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    //loginServiceImpl이 주입됨
    private final LoginService loginService;

    // JWT 토큰 생성 위해 필요
    private final JwtUtils jwtUtils;

    // ? 때문에 어떤 타입도 응답으로 설정 가능
    // form 로그인
    @PostMapping("/login/email")
    public ResponseEntity<?> emailLogin(@ModelAttribute EmailLoginRequestDto emailLoginRequestDto){

        MemberDTO member = loginService.checkForLogin(emailLoginRequestDto);

        if(member!= null){
            log.info("emailLogin response member : {}",member );

            String jwt = jwtUtils.createAccessToken(member.getEmail(),member.getName(),member.getMemberNo());
            log.info("jwt : {}",jwt);


            // resonse body에 담아서 보내는 방법
               return ResponseEntity.ok(jwt);

            // response header에 담아서 보내는 방법
            // return ResponseEntity.ok().header("Authorization","Bearer "+jwt).build();

        }else{
            log.info("emailLogin response member is null");

            // 응답 생성
            // ResponseEntity는 제네릭 클래스 (비워두면, 컴파일러가 자동으로 적절한 데이터 타입 추론)
            return ResponseEntity.badRequest().body("로그인 실패");

        }


    }

}
