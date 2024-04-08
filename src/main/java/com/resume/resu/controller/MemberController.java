package com.resume.resu.controller;

import com.resume.resu.service.api.MemberService;
import com.resume.resu.vo.response.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    public final MemberService memberService;

    @GetMapping("/resume/member/info/{memberNo}")
    public ResponseEntity<MemberDTO> getResumeMemberInfo(@PathVariable("memberNo") int memberNo){

        MemberDTO member = memberService.getResumeMemberInfo(memberNo);

        if(member == null){
            return ResponseEntity.badRequest().build();
        }

        log.info("getResumeMemberInfo의 joinDate : {}",member.getJoinDate());
        return ResponseEntity.ok(member);

    }

}
