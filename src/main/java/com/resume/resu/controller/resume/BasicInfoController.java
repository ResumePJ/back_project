package com.resume.resu.controller.resume;

import com.resume.resu.service.api.resume.BasicInfoService;
import com.resume.resu.util.JwtUtils;
import com.resume.resu.vo.response.MemberDTO;
import com.resume.resu.vo.response.ResumeBasicInfoDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BasicInfoController {

    public final BasicInfoService basicInfoService;
    public final JwtUtils jwtUtils;

    @GetMapping("/resume/basic/{resumeNo}")
    public ResponseEntity<ResumeBasicInfoDTO> getResumeBasicInfo(@PathVariable("resumeNo") int resumeNo, HttpServletRequest req){

        //TODO : 본인이 만든 이력서가 맞는지 확인 후, 이력서 내용 반환

        String accessToken=jwtUtils.getAcceessToken(req);
        int memberNo = jwtUtils.getMemberNo(accessToken);

        log.info("memberNo : {}",memberNo);

        // 토큰이 있고, 검증 성공한 토큰이어야 실행 -> 회원이지만 권한이 있는지 확인하기 위해 메서드 실행
        boolean isMyResume = basicInfoService.isMyResume(memberNo,resumeNo);

        // 내 이력서가 아닌 이력서를 조회하고자 하면 403 Forbidden 오류 발생
        if(!isMyResume){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        //내 이력서를 조회하고자 하면 이력서 내용 반환
        else{
            ResumeBasicInfoDTO resumeBasicInfoDTO = basicInfoService.getResumeBasicInfo(resumeNo);
            return ResponseEntity.ok(resumeBasicInfoDTO);
        }
    }



}
