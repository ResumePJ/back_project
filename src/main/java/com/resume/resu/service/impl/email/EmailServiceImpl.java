package com.resume.resu.service.impl.email;

import com.resume.resu.repository.email.EmailMapper;
import com.resume.resu.service.api.email.EmailService;
import com.resume.resu.vo.request.EmailAuthenticationRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender javaMailSender;
    private final EmailMapper emailMapper;

    @Override
    public int send(String email, String subject, String text, int code) {
        Long count = getEmailRequestCount(email);

        //5번 초과해 이메일 인증 요청 할 수 없음!
        if(count==5){
            return -1;
        }

        // SimpleMailMessage의 인스턴스로 메시지 설정
        // JavaMailSender의 인스턴스로 메일 전송
        SimpleMailMessage message= new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);

        saveVerificationCode(email,String.valueOf(code)); // 인증 코드 저장
        increaseEmailRequestCount(email); //요청 횟수 증가

        return 0;
    }

    @Override
    public boolean authenticateEmail(EmailAuthenticationRequestDto emailAuthenticationRequestDto) {
        // 인증 완료 되면, member DB에서 이름, 이메일, 생년월일 일치하는 데이터가 있는지 확인
        String code = getVerificationCode(emailAuthenticationRequestDto.getEmail());
        if(code==null){
            log.info("redis code is null");
            return false;
        }
        if(code.equals(emailAuthenticationRequestDto.getCode())){
            log.info("emailAuthentication success");
            String name=emailAuthenticationRequestDto.getName();

            //저장된 날짜 형식 으로 변환
            String birth=(emailAuthenticationRequestDto.getBirth()).substring(0,4)+"-"+(emailAuthenticationRequestDto.getBirth()).substring(4,6)+"-"+(emailAuthenticationRequestDto.getBirth()).substring(6,8);
            String email=emailAuthenticationRequestDto.getEmail();

            // 인증 성공이며 입력한 이름/생년월일/이메일의 데이터가 member 테이블에 존재할 때
            if(emailMapper.isMember(name,birth,email)==1){
                log.info("emailAuthentication success, data exist in member table");
                return true;
            }else {
                log.info("emailAuthentication success, data not exist in member table");
                return false;
            }
        }
        log.info("emailAuthentication unsuccess");
        return false;
    }

    // redis에서 인증코드 가져옴
    public String getVerificationCode(String email){
        log.info("get code : {}",redisTemplate.opsForValue().get(email));
        return redisTemplate.opsForValue().get(email);

    }

    // 이메일 요청 카운트 가져옴
    public Long getEmailRequestCount(String email){
        String key="email_request_count:"+email;
        String value=redisTemplate.opsForValue().get(key);
        return value != null? Long.parseLong(value):0;
    }

    // redis에 인증코드 저장
    public void saveVerificationCode(String email, String code){

        //2분 동안 redis에 저장된 데이터가 유효함
        redisTemplate.opsForValue().set(email,code,2, TimeUnit.MINUTES);
    }

    public void increaseEmailRequestCount(String email){
        String key="email_request_count:"+email;

        // 이메일 인증을 처음 요청했을 경우, redis에 key값을 키로 데이터를 생성한 후 value를 증가시킴
        Long count=redisTemplate.opsForValue().increment(key);

        // 5번 요청 시, 요청 횟수 데이터가 24시간 이후에 사라짐
        // 24시간 동안 더이상 요청 불가
        if (count == 5) {
            redisTemplate.expire(key, 24, TimeUnit.HOURS);
        }
    }
}
