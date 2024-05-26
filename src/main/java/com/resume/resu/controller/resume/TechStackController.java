package com.resume.resu.controller.resume;

import com.resume.resu.service.api.resume.BasicInfoService;
import com.resume.resu.service.api.resume.TechStackService;
import com.resume.resu.util.JwtUtils;
import com.resume.resu.vo.request.TechStackRequestDto;
import com.resume.resu.vo.request.TechStackRequestDtoList;
import com.resume.resu.vo.response.CareerResponseDto;
import com.resume.resu.vo.response.TechStackResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TechStackController {

    public final TechStackService techStackService;
    public final BasicInfoService basicInfoService;
    public final JwtUtils jwtUtils;


    /* 기술스택 추가
     * 하나의 이력서에 여러 개 기술스택 추가 가능
     * JSON 형식의 List<TechStackRequestDto> getListDto 데이터 받을 것!
     * techNo는 받아오지 않음. 자동으로 생성된 후 값이 대입됨(auto_increment)
     * */

    @PostMapping("/resume/techStack/{resumeNo}")
    public ResponseEntity<?> addTechStack(@PathVariable(name="resumeNo")int resumeNo, @ModelAttribute TechStackRequestDtoList getListDto, HttpServletRequest req){
        log.info("TechStackRequestDtoList : {}", getListDto);
        if(getListDto.getList()==null){
            log.info("dto가 null입니다.");
            return ResponseEntity.badRequest().body("dto가 null입니다.");
        }

        for(TechStackRequestDto dto : getListDto.getList()) {
            if(dto.getType()==-1 || dto.getTechName()==null || dto.getTechLevel()==-1){
                log.info("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
                return ResponseEntity.badRequest().body("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
            }
        }

        String accessToken = jwtUtils.getAcceessToken(req);

        //요청 헤더의 토근에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(basicInfoService.isMyResume(memberNo, resumeNo)){
            log.info("addTechStack의 getListDto : {}",getListDto);
            List<TechStackResponseDto> list = techStackService.addTechStack(getListDto, resumeNo);
            log.info("삽입한 경력 내역 : {}", list);
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


    /* 기술스택 수정
    *  하나의 기술스택에 대한 정보를 가져와, 해당 기술스택을 수정, 수정한 기술스택 하나만 반환
    * techNo까지 Dto에 받아와야함! */
    @PostMapping("/resume/techStack/update/{resumeNo}")
    public ResponseEntity<?> updateTechStack(@PathVariable(name="resumeNo") int resumeNo, @ModelAttribute TechStackRequestDto techStackRequestDto, HttpServletRequest req){

        // techNo는 기술 스택 번호 없음으로 오류 발생 잡힘
        if(techStackRequestDto.getType()==-1 || techStackRequestDto.getTechName()==null || techStackRequestDto.getTechLevel()==-1){
            log.info("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
            return ResponseEntity.badRequest().body("모든 데이터를 작성해 전송하세요! 빈 데이터 존재함");
        }

        String accessToken = jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(basicInfoService.isMyResume(memberNo, resumeNo)){

            // 해당 이력서의 기술스택이 맞는지 확인 (내 이력서중 해당 이력서의 기술스택인지 확인)
            if(techStackService.isResumeTechStack(techStackRequestDto.getTechNo(),resumeNo)){
                TechStackResponseDto result = techStackService.updateTechStack(techStackRequestDto);
                return ResponseEntity.ok(result);
            }
            else{
                //내 기술스택은 맞지만, 해당 이력서의 기술스택이 아님
                if(techStackService.isMyTechStack(techStackRequestDto.getTechNo(), memberNo)){
                    return ResponseEntity.badRequest().build();
                } else {
                    // 이력서는 내 것이고, 기술 스택은 존재하지만, 내 기술스택이 아닐 때
                    if(techStackService.isTechStack(techStackRequestDto.getTechNo())){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    log.info("기술스택 번호가 존재하지 않음");
                    // 기술스택이 아예 존재하지 않을 때
                    return ResponseEntity.badRequest().build();
                }
            }
        }
        // 내가 작성한 이력서가 아닐 경우
        else{
            // 존재하지 않는 이력서 번호라면?
            if(!basicInfoService.isResume(resumeNo)){
                log.info("기술스택 추가의 이력서 번호가 존재하지 않는 이력서 번호임");
                return ResponseEntity.badRequest().build();
            }

            // 실재하는 이력서 번호이지만, 내 이력서가 아님
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    /* 전체 기술스택 들고오기 */
    @GetMapping("/resume/techStack/full/{resumeNo}")
    public ResponseEntity<List<TechStackResponseDto>> getFullTechStack(@PathVariable(name="resumeNo")int resumeNo, HttpServletRequest req){
        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(basicInfoService.isMyResume(memberNo, resumeNo)){
            List<TechStackResponseDto> result = techStackService.getFullTechStack(resumeNo);

            // 리스트가 비어있는지 확인 => null이 아닌, isEmpty로 확인!
            if(result.isEmpty()){
                log.info("전체 기술스택 조회 : noContent");
                return ResponseEntity.noContent().build();
            }else{
                return ResponseEntity.ok(result);
            }
        }

        // 내가 작성한 이력서가 아닐 경우
        else{
            // 존재하지 않은 이력서 번호라면?
            if(!basicInfoService.isResume(resumeNo)){
                return ResponseEntity.badRequest().build();
            }

            // 실제하는 이력서 번호이지만, 내 이력서가 아님
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    /* 하나의 경럭 조회 */
    @GetMapping("/resume/techStack/one/{resumeNo}/{techNo}")
    public ResponseEntity<TechStackResponseDto> getOneTechStack(@PathVariable(name="resumeNo") int resumeNo, @PathVariable(name="techNo") int techNo, HttpServletRequest req){
        String accessToken=jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(basicInfoService.isMyResume(memberNo,resumeNo)){

            // 해당 이력서의 기술스택 번호가 맞는지 확인 (내 이력서 중 해당 이력서의 기술스택인지 확인)
            if(techStackService.isResumeTechStack(techNo, resumeNo)){
                TechStackResponseDto result = techStackService.getOneTechStack(techNo);
                return ResponseEntity.ok(result);
            }else {

                // 내 기술스택이 맞지만, 해당 이력서의 기술스택이 아님
                if(techStackService.isMyTechStack(techNo, memberNo)){
                    return ResponseEntity.badRequest().build();
                }else{
                    // 이력서는 내 것이고, 기술스택은 존재하지만, 내 기술스택이 아닐 때
                    if(techStackService.isTechStack(techNo)){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    // 기술스택이 아예 존재하지 않을 때
                    return ResponseEntity.badRequest().build();
                }
            }
        }

        // 내가 작성한 이력서가 아닐 경우
        else{
            //존재하지 않은 이력서 번호라면?
            if(!basicInfoService.isResume(resumeNo)){
                return ResponseEntity.badRequest().build();
            }

            // 실재하는 이력서 번호지만, 내 이력서가 아님
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    /* 하나의 기술스택 삭제
            정상적으로 삭제 시, 해당 이력서의 나머지 존재하는 기술스택 list 반환
         */
    @DeleteMapping("/resume/techStack/delete/{resumeNo}/{techNo}")
    public ResponseEntity<List<TechStackResponseDto>>deleteTechStack(@PathVariable(name="resumeNo")int resumeNo, @PathVariable(name="techNo") int techNo,HttpServletRequest req){
        String accessToken = jwtUtils.getAcceessToken(req);

        // 요청 헤더의 토큰에서 memberNo 가져옴
        int memberNo = jwtUtils.getMemberNo(accessToken);

        // 내가 작성한 이력서가 맞다면 실행
        if(basicInfoService.isMyResume(memberNo, resumeNo)){

            // 해당 이력서의 기술스택 번호가 맞는지 확인 (내 이력서중 해당 이력서의 기술스택인지 확인)
            if(techStackService.isResumeTechStack(techNo,resumeNo)){
                List<TechStackResponseDto> result = techStackService.deleteTechStack(techNo,resumeNo);

                // 삭제 후, 해당 이력서에 존재하는 모든 기술스택들의 list 반환
                return ResponseEntity.ok(result);
            }
            else {
                // 이력서는 내 것이고, 내 기술스택은 맞지만, 해당 이력서의 기술스택이 아님
                if(techStackService.isMyTechStack(techNo,memberNo)){
                    return ResponseEntity.badRequest().build();
                }
                else{
                    // 이력서는 내 것이고, 기술스택은 존재하지만, 내 기술스택이 아닐 때
                    if(techStackService.isTechStack(techNo)){
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    }
                    // 기술스택이 아예 존재하지 않을 때
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
