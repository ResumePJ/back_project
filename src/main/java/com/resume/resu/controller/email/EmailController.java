package com.resume.resu.controller.email;

import com.resume.resu.service.api.email.EmailService;
import com.resume.resu.vo.request.EmailAuthenticationRequestDto;
import com.resume.resu.vo.request.EmailSendRequestDto;
import com.resume.resu.vo.response.EmailAuthenticationResponseDto;
import com.resume.resu.vo.response.EmailSendResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/member/email/send")
    public ResponseEntity<?> sendEmail(@ModelAttribute EmailSendRequestDto emailSendRequestDto){
        if(emailSendRequestDto.getEmail()==null){
            log.info("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
            return ResponseEntity.badRequest().body("모든 데이터를 작성해 전송하세요!");
        }

        String subject=" RESU 이메일 인증 이메일 입니다.";
        Random random = new Random();

        // random 객체를 이용해 0~89999까지의 랜덤한 정수 생성 +10000 => 10000~99999까지로 범위 조정
        int code = random.nextInt(90000)+10000;

        String text = "인증 코드는 "+code+"입니다.";
        int sendResult = emailService.send(emailSendRequestDto.getEmail(),subject,text,code);

        // 400 오류 발생 시, emailSendResponseDto 반환
        // TODO : 오류 처리 시, 반환 Dto 사용할 것!
        if(sendResult==-1){
            EmailSendResponseDto emailSendResponseDto = new EmailSendResponseDto(emailSendRequestDto.getEmail(),"이메일 인증을 5회 요청 하셨습니다. 24시간 이후 다시 요청하세요");
            log.info("count 5 over!");
            return ResponseEntity.badRequest().body(emailSendResponseDto);
        }else{
            EmailSendResponseDto emailSendResponseDto = new EmailSendResponseDto(emailSendRequestDto.getEmail());
            return ResponseEntity.ok(emailSendResponseDto);
        }

    }

    // 비밀번호 재설정시 이메일을 이용한 본인 인증
    // 이메일은 중복 불가능하기 때문에, 이메일을 클라이언트에게 반환해주어 어떤 계정인지 알 수있도록 함
    @PostMapping("/member/email/authentication")
    public ResponseEntity<?> authenticateEmail(@ModelAttribute EmailAuthenticationRequestDto emailAuthenticationRequestDto){
       if(emailAuthenticationRequestDto.getName()==null ||
               emailAuthenticationRequestDto.getBirth()==null ||
               emailAuthenticationRequestDto.getEmail()==null || emailAuthenticationRequestDto.getCode()==null){
           log.info("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
           return ResponseEntity.badRequest().body("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
       }
       boolean result =  emailService.authenticateEmail(emailAuthenticationRequestDto);
       if(result){
           EmailAuthenticationResponseDto emailAuthenticationResponseDto=new EmailAuthenticationResponseDto(emailAuthenticationRequestDto.getEmail());
           return ResponseEntity.ok(emailAuthenticationResponseDto);
       }else{
            return ResponseEntity.badRequest().build();
       }
    }


}
