package com.resume.resu.controller.resume;

import com.resume.resu.service.api.resume.BasicInfoService;
import com.resume.resu.service.api.resume.PortfolioService;
import com.resume.resu.util.JwtUtils;
import com.resume.resu.vo.request.ExperienceRequestDto;
import com.resume.resu.vo.request.PortfolioRequestDto;
import com.resume.resu.vo.request.PortfolioRequestDtoList;
import com.resume.resu.vo.response.CareerResponseDto;
import com.resume.resu.vo.response.PortfolioResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
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
    public ResponseEntity<?> addPortfolio(@PathVariable(name="resumeNo") int resumeNo, @ModelAttribute PortfolioRequestDtoList getListDto, HttpServletRequest req){

        if(getListDto.getList()==null){
            log.info("dto가 null입니다.");
            return ResponseEntity.badRequest().body("dto가 null입니다.");

        }

        for(PortfolioRequestDto dto:getListDto.getList()){
            if(dto.getTitle()==null|| dto.getLink()==null || dto.getPofolDetail()==null){
                log.info("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
                return ResponseEntity.badRequest().body("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
            }
        }

        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(basicInfoService.isMyResume(memberNo,resumeNo)){
            log.info("addPortfolio의 getListDto : {}",getListDto);
            List<PortfolioResponseDto> list = portfolioService.addPortfolio(getListDto,resumeNo);
            log.info("삽입한 포폴 내역 : {}",list );
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

    /* 하나의 포토폴리오 수정
    하나의 포폴에 대한 정보를 가져와, 해당 포폴을 수정, 수정한 포폴 하나만 반환
    pofolNo까지 같이 form으로 전달해야함
    TODO : 프론트엔드 작업하면서 수정해야할 경우, 수정 할 것 */
    @PostMapping("/resume/portfolio/update/{resumeNo}")
    public ResponseEntity<?> updatePortfolio(@PathVariable(name="resumeNo") int resumeNo, @ModelAttribute PortfolioRequestDto portfolioRequestDto, HttpServletRequest req){

        // pofolNo가 비어있을 경우, 아래쪽 포폴 번호가 없음 부분에서 걸림!
        if(portfolioRequestDto.getTitle()==null|| portfolioRequestDto.getLink()==null || portfolioRequestDto.getPofolDetail()==null){
            log.info("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
            return ResponseEntity.badRequest().body("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
        }

        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(basicInfoService.isMyResume(memberNo,resumeNo)){

            // 해당 이력서의 포폴 번호가 맞는지 확인 (내 이력서중 해당 이력서의 포폴인지 확인)
            if(portfolioService.isResumePofol(portfolioRequestDto.getPofolNo(),resumeNo)){
                PortfolioResponseDto result = portfolioService.updatePortfolio(portfolioRequestDto);
                return ResponseEntity.ok(result);
            }
            else {
                // 내 포폴은 맞지만, 해당 이력서의 포폴이 아님
                if(portfolioService.isMyPofol(portfolioRequestDto.getPofolNo(),memberNo)){
                    return ResponseEntity.badRequest().build();
                }
                else{
                    // 이력서는 내 것이고, 포폴은 존재하지만, 내 포폴이 아닐 때
                    if(portfolioService.isPofol(portfolioRequestDto.getPofolNo())){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    // 포폴이 아예 존재하지 않을 때
                    log.info("포폴 번호가 존재하지 않음!");
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

    // 전체 포폴 조회
    @GetMapping("/resume/portfolio/full/{resumeNo}")
    public ResponseEntity<List<PortfolioResponseDto>> getFullPortfolio(@PathVariable(name="resumeNo")int resumeNo, HttpServletRequest req){
        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(basicInfoService.isMyResume(memberNo,resumeNo)){
            List<PortfolioResponseDto> result = portfolioService.getFullPortfolio(resumeNo);

            // 리스트가 비어있는지 확인 => null이 아닌, isEmpty로 확인!
            if(result.isEmpty()){
                log.info("전체 경력 조회 : noContent");
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

    // 하나의 포폴 조회
    @GetMapping("/resume/portfolio/one/{resumeNo}/{pofolNo}")
    public ResponseEntity<PortfolioResponseDto> getOnePortfolio(@PathVariable(name="resumeNo")int resumeNo, @PathVariable(name="pofolNo")int pofolNo, HttpServletRequest req){

        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(basicInfoService.isMyResume(memberNo,resumeNo)){

            // 해당 이력서의 포폴 번호가 맞는지 확인 (내 이력서중 해당 이력서의 포폴인지 확인)
            if(portfolioService.isResumePofol(pofolNo,resumeNo)){
                PortfolioResponseDto result = portfolioService.getOnePofol(pofolNo);
                return ResponseEntity.ok(result);
            }
            else {
                // 내 포폴은 맞지만, 해당 이력서의 포폴이 아님
                if(portfolioService.isMyPofol(pofolNo,memberNo)){
                    return ResponseEntity.badRequest().build();
                }
                else{
                    // 이력서는 내 것이고, 포폴은 존재하지만, 내 포폴이 아닐 때
                    if(portfolioService.isPofol(pofolNo)){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    // 포폴이 아예 존재하지 않을 때
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

    // 하나의 포폴 삭제
    @DeleteMapping("/resume/portfolio/delete/{resumeNo}/{pofolNo}")
    public ResponseEntity<List<PortfolioResponseDto>> deletePortfolio(@PathVariable(name="resumeNo")int resumeNo, @PathVariable(name="pofolNo")int pofolNo, HttpServletRequest req){
        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(basicInfoService.isMyResume(memberNo,resumeNo)){

            // 해당 이력서의 포폴 번호가 맞는지 확인 (내 이력서중 해당 이력서의 포폴인지 확인)
            if(portfolioService.isResumePofol(pofolNo,resumeNo)){
                List<PortfolioResponseDto> result = portfolioService.deletePortfolio(pofolNo,resumeNo);
                return ResponseEntity.ok(result);
            }
            else {
                // 내 포폴은 맞지만, 해당 이력서의 포폴이 아님
                if(portfolioService.isMyPofol(pofolNo,memberNo)){
                    return ResponseEntity.badRequest().build();
                }
                else{
                    // 이력서는 내 것이고, 포폴은 존재하지만, 내 포폴이 아닐 때
                    if(portfolioService.isPofol(pofolNo)){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    // 포폴이 아예 존재하지 않을 때
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
