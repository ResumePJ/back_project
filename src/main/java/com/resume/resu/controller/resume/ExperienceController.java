package com.resume.resu.controller.resume;

import com.resume.resu.service.api.resume.BasicInfoService;
import com.resume.resu.service.api.resume.ExperienceService;
import com.resume.resu.util.JwtUtils;
import com.resume.resu.vo.request.ExperienceRequestDto;
import com.resume.resu.vo.response.ExperienceResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ExperienceController {
    public final ExperienceService experienceService;
    public final JwtUtils jwtUtils;

    //경험 추가
    //하나의 이력서에 대해 여러개의 경험 추가 가능
    // JSON 형식의 List<ExperienceRequestDto> getListDto 데이터를 받아야함 !
    // exNo는 받아오지 않음. 자동으로 생성됨 (auto_increment)
    @PostMapping ("/resume/experience/{resumeNo}")
    public ResponseEntity<List<ExperienceResponseDto>> addExperience(@PathVariable(name="resumeNo")int resumeNo, @RequestBody List<ExperienceRequestDto> getListDto, HttpServletRequest req){
        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(experienceService.isMyResume(memberNo,resumeNo)){
            log.info("addExperience의 getListDto : {}",getListDto);
            List<ExperienceResponseDto> list = experienceService.addExperience(getListDto,resumeNo);

            // 경험 정보 삽입 후, 삽입 정보 반환 했을 경우
            if(list != null){
                return ResponseEntity.ok(list);
            }
            // 경험 정보 삽입 시, 오류 발생
            else{
                return ResponseEntity.badRequest().build();
            }
        }
        // 내가 작성한 이력서가 아니면?
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // 경험 수정
    // 하나의 경험에 대한 정보를 가져와, 해당 경험을 수정, 수정한 경험 하나만 반환
    // exNo까지 Dto에 받아와야함!
    /* TODO : 프론트엔드 작업하면서 수정해야할 경우, 수정 할 것*/
    @PostMapping("/resume/experience/update/{resumeNo}")
    public ResponseEntity<ExperienceResponseDto> updateExperience(@PathVariable(name="resumeNo") int resumeNo,@ModelAttribute ExperienceRequestDto experienceRequestDto, HttpServletRequest req){
        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(experienceService.isMyResume(memberNo,resumeNo)){
            ExperienceResponseDto result = experienceService.updateExperience(experienceRequestDto);
            return ResponseEntity.ok(result);
        }
        // 내가 작성한 이력서가 아니면?
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

}
