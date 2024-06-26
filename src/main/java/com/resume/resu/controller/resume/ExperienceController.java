package com.resume.resu.controller.resume;

import com.resume.resu.service.api.resume.BasicInfoService;
import com.resume.resu.service.api.resume.ExperienceService;
import com.resume.resu.util.JwtUtils;
import com.resume.resu.vo.request.CareerRequestDto;
import com.resume.resu.vo.request.ExperienceRequestDto;
import com.resume.resu.vo.request.ExperienceRequestDtoList;
import com.resume.resu.vo.response.ExperienceResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
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

@RestController
@Slf4j
@RequiredArgsConstructor
public class ExperienceController {
    public final ExperienceService experienceService;
    public final BasicInfoService basicInfoService;
    public final JwtUtils jwtUtils;

    //경험 추가
    //하나의 이력서에 대해 여러개의 경험 추가 가능
    // JSON 형식의 List<ExperienceRequestDto> getListDto 데이터를 받아야함 !
    // exNo는 받아오지 않음. 자동으로 생성됨 (auto_increment)
    @PostMapping ("/resume/experience/{resumeNo}")
    public ResponseEntity<?> addExperience(@PathVariable(name="resumeNo")int resumeNo, @ModelAttribute ExperienceRequestDtoList getListDto, HttpServletRequest req){

        if(getListDto.getList()==null){
            log.info("dto가 null입니다.");
            return ResponseEntity.badRequest().body("dto가 null입니다.");

        }

        for(ExperienceRequestDto dto:getListDto.getList()){
            if(dto.getExStartDate()==null|| dto.getExEndDate()==null || dto.getExName()==null ||
                    dto.getExDetail()==null ){
                log.info("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
                return ResponseEntity.badRequest().body("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
            }
        }

        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(experienceService.isMyResume(memberNo,resumeNo)){
            log.info("addExperience의 getListDto : {}",getListDto);
            List<ExperienceResponseDto> list = experienceService.addExperience(getListDto,resumeNo);
            return ResponseEntity.ok(list);
        }

        // 내가 작성한 이력서가 아닐 경우
        else{

            // 존재하지 않는 이력서 번호라면?
            if(!basicInfoService.isResume(resumeNo)){
                return ResponseEntity.badRequest().build();
            }

            // 실재하는 이력서 번호이지만, 내 이력서가 아님
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // 경험 수정
    // 하나의 경험에 대한 정보를 가져와, 해당 경험을 수정, 수정한 경험 하나만 반환
    // exNo까지 Dto에 받아와야함!
    /* TODO : 프론트엔드 작업하면서 수정해야할 경우, 수정 할 것*/
    @PostMapping("/resume/experience/update/{resumeNo}")
    public ResponseEntity<?> updateExperience(@PathVariable(name="resumeNo") int resumeNo,@ModelAttribute ExperienceRequestDto experienceRequestDto, HttpServletRequest req){

        if(experienceRequestDto.getExStartDate()==null|| experienceRequestDto.getExEndDate()==null || experienceRequestDto.getExName()==null ||
                experienceRequestDto.getExDetail()==null ){
            log.info("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
            return ResponseEntity.badRequest().body("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
        }

        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(experienceService.isMyResume(memberNo,resumeNo)){

            // 해당 이력서의 경험 번호가 맞는지 확인 (내 이력서중 해당 이력서의 경험인지 확인)
            if(experienceService.isResumeEx(experienceRequestDto.getExNo(),resumeNo)){
                ExperienceResponseDto result = experienceService.updateExperience(experienceRequestDto);
                return ResponseEntity.ok(result);
            }
            else {
                // 내 경험은 맞지만, 해당 이력서의 경험이 아님
                if(experienceService.isMyEx(experienceRequestDto.getExNo(),memberNo)){
                    return ResponseEntity.badRequest().build();
                }
                else{
                    // 이력서는 내 것이고, 경험은 존재하지만, 내 경험이 아닐 때
                    if(experienceService.isEx(experienceRequestDto.getExNo())){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    log.info("경험번호가 아예 존재하지 않음!");
                    // 경험이 아예 존재하지 않을 때
                    return ResponseEntity.badRequest().build();
                }
            }
        }
        // 내가 작성한 이력서가 아닐 경우
        else{

            // 존재하지 않는 이력서 번호라면?
            if(!basicInfoService.isResume(resumeNo)){
                return ResponseEntity.badRequest().build();
            }

            // 실재하는 이력서 번호이지만, 내 이력서가 아님
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    //전체 경험 조회
    @GetMapping("/resume/experience/full/{resumeNo}")
    public ResponseEntity<List<ExperienceResponseDto>> getFullExperience(@PathVariable(name="resumeNo") int resumeNo, HttpServletRequest req){
        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(experienceService.isMyResume(memberNo,resumeNo)){
            List<ExperienceResponseDto> result = experienceService.getFullExperience(resumeNo);

            // 리스트가 비어있는지 확인 => null이 아닌, isEmpty로 확인!
            if(result.isEmpty()){
                log.info("전체 경험 조회 : noContent");
                return ResponseEntity.noContent().build();
            }else{
                return ResponseEntity.ok(result);
            }
        }

        // 내가 작성한 이력서가 아닐 경우
        else{

            // 존재하지 않는 이력서 번호라면?
            if(!basicInfoService.isResume(resumeNo)){
                return ResponseEntity.badRequest().build();
            }

            // 실재하는 이력서 번호이지만, 내 이력서가 아님
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //하나의 경험 조회
    @GetMapping("/resume/experience/one/{resumeNo}/{exNo}")
    public ResponseEntity<ExperienceResponseDto> getOneExperience(@PathVariable(name="resumeNo") int resumeNo,@PathVariable(name = "exNo")int exNo, HttpServletRequest req){
        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(experienceService.isMyResume(memberNo,resumeNo)){

            // 해당 이력서의 경험 번호가 맞는지 확인 (내 이력서중 해당 이력서의 경험인지 확인)
            if(experienceService.isResumeEx(exNo,resumeNo)){
                ExperienceResponseDto result = experienceService.getOneExperience(exNo);
                return ResponseEntity.ok(result);
            }
            else {
                // 내 경험은 맞지만, 해당 이력서의 경험이 아님
                if(experienceService.isMyEx(exNo,memberNo)){
                    return ResponseEntity.badRequest().build();
                }
                else{
                    // 이력서는 내 것이고, 경험은 존재하지만, 내 경험이 아닐 때
                    if(experienceService.isEx(exNo)){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    // 경험이 아예 존재하지 않을 때
                    return ResponseEntity.badRequest().build();
                }
            }
        }
        // 내가 작성한 이력서가 아닐 경우
        else{

            // 존재하지 않는 이력서 번호라면?
            if(!basicInfoService.isResume(resumeNo)){
                return ResponseEntity.badRequest().build();
            }

            // 실재하는 이력서 번호이지만, 내 이력서가 아님
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /* 하나의 경험 삭제
     정상적으로 삭제 시, 해당 이력서의 나머지 존재하는 경험 list 반환 */
    @DeleteMapping("/resume/experience/delete/{resumeNo}/{exNo}")
    public ResponseEntity<List<ExperienceResponseDto>>deleteExperience(@PathVariable(name="resumeNo")int resumeNo, @PathVariable(name="exNo") int exNo,HttpServletRequest req){
        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(experienceService.isMyResume(memberNo,resumeNo)){

            // 해당 이력서의 경험 번호가 맞는지 확인 (내 이력서중 해당 이력서의 경험인지 확인)
            if(experienceService.isResumeEx(exNo,resumeNo)){
                 List<ExperienceResponseDto> result = experienceService.deleteExperience(exNo,resumeNo);

                // 삭제 후, 해당 이력서에 존재하는 모든 경험들의 list 반환
                return ResponseEntity.ok(result);
            }
            else {
                // 이력서는 내 것이고, 내 경험은 맞지만, 해당 이력서의 경험이 아님
                if(experienceService.isMyEx(exNo,memberNo)){
                    return ResponseEntity.badRequest().build();
                }
                else{
                    // 이력서는 내 것이고, 경험은 존재하지만, 내 경험이 아닐 때
                    if(experienceService.isEx(exNo)){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    // 경험이 아예 존재하지 않을 때
                    return ResponseEntity.badRequest().build();
                }
            }
        }
        // 내가 작성한 이력서가 아닐 경우
        else{

            // 존재하지 않는 이력서 번호라면?
            if(!basicInfoService.isResume(resumeNo)){
                return ResponseEntity.badRequest().build();
            }

            // 실재하는 이력서 번호이지만, 내 이력서가 아님
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
