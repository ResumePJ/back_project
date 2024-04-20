package com.resume.resu.controller.resume;

import com.resume.resu.service.api.resume.BasicInfoService;
import com.resume.resu.service.api.resume.PortfolioService;
import com.resume.resu.util.JwtUtils;
import com.resume.resu.vo.request.PortfolioRequestDto;
import com.resume.resu.vo.response.CareerResponseDto;
import com.resume.resu.vo.response.PortfolioResponseDto;
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

@Slf4j
@Controller
@RequiredArgsConstructor
public class PortfolioController {

    public final JwtUtils jwtUtils;
    public final BasicInfoService basicInfoService;
    public final PortfolioService portfolioService;

    /* 포폴 추가
    * 하나의 이력서에 여러 개 포폴 추가 가능
    * JSON 형식의 List<PortfolioRequestDto> getListDto 데이터 받을 것!
    * pofolNo는 요청 데이터로 받아오지 않음. 자동으로 생성 된 후, 값이 대입 될 예정 (auto_increment)
    * */

    @PostMapping("/resume/portfolio/{resumeNo}")
    public ResponseEntity<List<PortfolioResponseDto>> addPortfolio(@PathVariable(name="resumeNo") int resumeNo, @RequestBody List<PortfolioRequestDto> getListDto, HttpServletRequest req){
        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(basicInfoService.isMyResume(memberNo,resumeNo)){
            log.info("addPortfolio의 getListDto : {}",getListDto);
            List<PortfolioResponseDto> list = portfolioService.addPortfolio(getListDto,resumeNo);

            // 포폴 정보 삽입 후, 삽입 정보 반환 했을 경우
            if(list != null){
                return ResponseEntity.ok(list);
            }
            // 포폴 정보 삽입 시, 오류 발생
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
