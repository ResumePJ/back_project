package com.resume.resu.controller.resume;

import com.resume.resu.service.api.resume.BasicInfoService;
import com.resume.resu.service.api.resume.CareerService;
import com.resume.resu.service.api.resume.ExperienceService;
import com.resume.resu.util.JwtUtils;
import com.resume.resu.vo.request.CareerRequestDto;
import com.resume.resu.vo.request.ExperienceRequestDto;
import com.resume.resu.vo.response.CareerResponseDto;
import com.resume.resu.vo.response.ExperienceResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CareerController {

    public final CareerService careerService;
    public final BasicInfoService basicInfoService;
    public final JwtUtils jwtUtils;

    // 경력 추가
    // 하나의 이력서에 대해 여러개의 경력 추가 가능
    // JSON 형식의 List<CareerRequestDto> getListDto 데이터를 받아야함 !
    // carNo는 받아오지 않음. 자동으로 생성된 후 값이 대입됨(auto_increment)
    @PostMapping("/resume/career/{resumeNo}")
    public ResponseEntity<List<CareerResponseDto>> addCareer(@PathVariable(name="resumeNo")int resumeNo, @RequestBody List<CareerRequestDto> getListDto, HttpServletRequest req){
        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(basicInfoService.isMyResume(memberNo,resumeNo)){
            log.info("addCareer의 getListDto : {}",getListDto);
            List<CareerResponseDto> list = careerService.addCareer(getListDto,resumeNo);

            // 경력 정보 삽입 후, 삽입 정보 반환 했을 경우
            if(list != null){
                return ResponseEntity.ok(list);
            }
            // 경력 정보 삽입 시, 오류 발생
            else{
                return ResponseEntity.badRequest().build();
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
