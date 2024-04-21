package com.resume.resu.controller.member;

import com.resume.resu.service.api.member.MemberService;
import com.resume.resu.vo.request.MemberRequestDto;
import com.resume.resu.vo.response.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    public final MemberService memberService;

    // 로그인 전, 회원 정보 조회 ( 휴대폰 번호 / 이메일 / 사원번호 )
    @PostMapping("/member/info")
    public ResponseEntity<MemberDTO> getMemberInfo(@ModelAttribute MemberRequestDto memberRequestDto){
        MemberDTO member = memberService.getMemberInfo(memberRequestDto);

        // 찾는 회원 정보 없음 400 Bad Request
        if(member==null){
            return ResponseEntity.badRequest().build();
        }else{
            return ResponseEntity.ok(member);
        }
    }
}
