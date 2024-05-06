package com.resume.resu.controller.resume;

import com.resume.resu.service.api.resume.BasicInfoService;
import com.resume.resu.service.api.resume.CareerService;
import com.resume.resu.service.api.resume.ExperienceService;
import com.resume.resu.util.JwtUtils;
import com.resume.resu.vo.request.CareerRequestDto;
import com.resume.resu.vo.request.CareerRequestDtoList;
import com.resume.resu.vo.request.ExperienceRequestDto;
import com.resume.resu.vo.response.CareerResponseDto;
import com.resume.resu.vo.response.ExperienceResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    public ResponseEntity<?> addCareer(@PathVariable(name="resumeNo")int resumeNo, @ModelAttribute CareerRequestDtoList getListDto, HttpServletRequest req){
        log.info("CarRequestDtoList : {}",getListDto);
        if(getListDto.getList()==null){
            log.info("dto가 null입니다.");
            return ResponseEntity.badRequest().body("dto가 null입니다.");
        }

        for(CareerRequestDto dto:getListDto.getList()){
            if(dto.getCompany()==null|| dto.getDept()==null || dto.getPosition()==null ||
               dto.getCarStartDate()==null || dto.getCarEndDate()==null || dto.getCarDetail()==null){
                log.info("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
                return ResponseEntity.badRequest().body("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
            }
        }

        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(basicInfoService.isMyResume(memberNo,resumeNo)){
            log.info("addCareer의 getListDto : {}",getListDto);
            List<CareerResponseDto> list = careerService.addCareer(getListDto,resumeNo);
            log.info("삽입한 경력 내역 : {}",list );
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

    // 경력 수정
    // 하나의 경력에 대한 정보를 가져와, 해당 경력을 수정, 수정한 경력 하나만 반환
    // carNo까지 Dto에 받아와야함!
    @PostMapping("/resume/career/update/{resumeNo}")
    public ResponseEntity<?> updateCareer(@PathVariable(name="resumeNo") int resumeNo, @ModelAttribute CareerRequestDto careerRequestDto, HttpServletRequest req){

        // carNo는 경력 번호 없음으로 오류 발생 잡힘
        if(careerRequestDto.getCompany()==null|| careerRequestDto.getDept()==null || careerRequestDto.getPosition()==null ||
                careerRequestDto.getCarStartDate()==null || careerRequestDto.getCarEndDate()==null || careerRequestDto.getCarDetail()==null){
            log.info("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
            return ResponseEntity.badRequest().body("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
        }

        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(basicInfoService.isMyResume(memberNo,resumeNo)){

            // 해당 이력서의 경력 번호가 맞는지 확인 (내 이력서중 해당 이력서의 경력인지 확인)
            if(careerService.isResumeCareer(careerRequestDto.getCarNo(),resumeNo)){
                CareerResponseDto result = careerService.updateCareer(careerRequestDto);
                return ResponseEntity.ok(result);
            }
            else {
                // 내 경력은 맞지만, 해당 이력서의 경력이 아님
                if(careerService.isMyCareer(careerRequestDto.getCarNo(),memberNo)){
                    return ResponseEntity.badRequest().build();
                }
                else{
                    // 이력서는 내 것이고, 경력은 존재하지만, 내 경력이 아닐 때
                    if(careerService.isCareer(careerRequestDto.getCarNo())){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    log.info("경력 번호가 존재하지 않음");
                    // 경력이 아예 존재하지 않을 때
                    return ResponseEntity.badRequest().build();
                }
            }
        }
        // 내가 작성한 이력서가 아닐 경우
        else{

            // 존재하지 않는 이력서 번호라면?
            if(!basicInfoService.isResume(resumeNo)){
                log.info("경력 추가의 이력서 번호가 존재하지 않는 이력서 번호임");
                return ResponseEntity.badRequest().build();
            }

            // 실재하는 이력서 번호이지만, 내 이력서가 아님
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    //전체 경력 들고오기
    @GetMapping("/resume/career/full/{resumeNo}")
    public ResponseEntity<List<CareerResponseDto>> getFullCareer(@PathVariable(name="resumeNo")int resumeNo, HttpServletRequest req){
        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(basicInfoService.isMyResume(memberNo,resumeNo)){
            List<CareerResponseDto> result = careerService.getFullCareer(resumeNo);

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

    // 하나의 경력 조회
    @GetMapping("/resume/career/one/{resumeNo}/{carNo}")
    public ResponseEntity<CareerResponseDto> getOneCareer(@PathVariable(name="resumeNo") int resumeNo, @PathVariable(name="carNo") int carNo, HttpServletRequest req){
        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(basicInfoService.isMyResume(memberNo,resumeNo)){

            // 해당 이력서의 경력 번호가 맞는지 확인 (내 이력서 중 해당 이력서의 경력인지 확인)
            if(careerService.isResumeCareer(carNo,resumeNo)){
                CareerResponseDto result = careerService.getOneCareer(carNo);
                return ResponseEntity.ok(result);
            }
            else {
                // 내 경력은 맞지만, 해당 이력서의 경력이 아님
                if(careerService.isMyCareer(carNo,memberNo)){
                    return ResponseEntity.badRequest().build();
                }
                else{
                    // 이력서는 내 것이고, 경력은 존재하지만, 내 경력이 아닐 때
                    if(careerService.isCareer(carNo)){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    // 경력이 아예 존재하지 않을 때
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

    /* 하나의 경력 삭제
     정상적으로 삭제 시, 해당 이력서의 나머지 존재하는 경력 list 반환 */
    @DeleteMapping("/resume/career/delete/{resumeNo}/{carNo}")
    public ResponseEntity<List<CareerResponseDto>>deleteCareer(@PathVariable(name="resumeNo")int resumeNo, @PathVariable(name="carNo") int carNo,HttpServletRequest req){
        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(basicInfoService.isMyResume(memberNo,resumeNo)){

            // 해당 이력서의 경력 번호가 맞는지 확인 (내 이력서중 해당 이력서의 경력인지 확인)
            if(careerService.isResumeCareer(carNo,resumeNo)){
                List<CareerResponseDto> result = careerService.deleteCareer(carNo,resumeNo);

                // 삭제 후, 해당 이력서에 존재하는 모든 경력들의 list 반환
                return ResponseEntity.ok(result);
            }
            else {
                // 이력서는 내 것이고, 내 경력은 맞지만, 해당 이력서의 경력이 아님
                if(careerService.isMyCareer(carNo,memberNo)){
                    return ResponseEntity.badRequest().build();
                }
                else{
                    // 이력서는 내 것이고, 경력은 존재하지만, 내 경력이 아닐 때
                    if(careerService.isCareer(carNo)){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    // 경력이 아예 존재하지 않을 때
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
