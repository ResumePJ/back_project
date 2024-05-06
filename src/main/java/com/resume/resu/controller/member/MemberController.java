package com.resume.resu.controller.member;

import com.resume.resu.service.api.member.MemberService;
import com.resume.resu.util.JwtUtils;
import com.resume.resu.vo.request.MemberRequestDto;
import com.resume.resu.vo.request.UpdateMemberRequestDto;
import com.resume.resu.vo.response.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    public final MemberService memberService;
    public final JwtUtils jwtUtils;

    // 로그인 전, 회원 정보 조회 ( 휴대폰 번호 / 이메일 / 사원번호 )
    @PostMapping("/member/info")
    public ResponseEntity<?> getMemberInfo(@ModelAttribute MemberRequestDto memberRequestDto){
        if (memberRequestDto.getIdentifier() == null || memberRequestDto.getData() == null) {
            log.info("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
            return ResponseEntity.badRequest().body("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
        }

        MemberDTO member = memberService.getMemberInfo(memberRequestDto);

        // 찾는 회원 정보 없음 400 Bad Request
        if(member==null){
            return ResponseEntity.badRequest().build();
        }else{
            return ResponseEntity.ok(member);
        }
    }

    // 로그인 후, 나의 회원 번호를 이용한 정보 조회
    @GetMapping("/member/info/login")
    public ResponseEntity<MemberDTO> getMemberInfoAfterLogin( HttpServletRequest req ){

        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        MemberDTO member = memberService.getMemberInfoAfterLogin(memberNo);

        // 찾는 회원 정보 없음 400 Bad Request
        if(member==null){
            return ResponseEntity.badRequest().build();
        }else{
            return ResponseEntity.ok(member);
        }
    }

    // 내 정보 수정
    /*TODO : 프론트 작업 할 때, type을 입력하지 않으면 자동으로 0응로 처리됨 -> 반드시 type 값까지 넣어서 전송해야함
    *  의도하지않은 0으로 처리될 수 있음!*/
    @PostMapping("/member/info/update")
    public ResponseEntity<?> updateMemberInfo(@ModelAttribute UpdateMemberRequestDto updateMemberRequestDto, HttpServletRequest req){

        if(updateMemberRequestDto.getEmail()==null||updateMemberRequestDto.getPassword()==null|| updateMemberRequestDto.getName()==null||
           updateMemberRequestDto.getPhone()==null||updateMemberRequestDto.getBirth()==null|| updateMemberRequestDto.getJoinDate()==null){
            log.info("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
            return ResponseEntity.badRequest().body("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
        }

        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        MemberDTO member = memberService.updateMemberInfo(updateMemberRequestDto, memberNo);

        if(member==null){
            return ResponseEntity.badRequest().build();
        }else{
            return ResponseEntity.ok(member);
        }

    }


}
