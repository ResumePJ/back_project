package com.resume.resu.service.impl.resume;

import com.resume.resu.repository.resume.TechStackMapper;
import com.resume.resu.service.api.resume.TechStackService;
import com.resume.resu.vo.request.TechStackRequestDto;
import com.resume.resu.vo.request.TechStackRequestDtoList;
import com.resume.resu.vo.response.TechStackResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TechStackServiceImpl implements TechStackService {

    public final TechStackMapper techStackMapper;

    @Override
    public List<TechStackResponseDto> addTechStack(TechStackRequestDtoList getListDto, int resumeNo) {
        List<TechStackResponseDto> resultList = new ArrayList<>();

        for (TechStackRequestDto techStackRequestDto : getListDto.getList()) {
            techStackMapper.addTechStack(techStackRequestDto,resumeNo);

            log.info("techStackRequestDto의 techNo : {}", techStackRequestDto.getTechNo());
            TechStackResponseDto result = techStackMapper.findTechStackByTechNo(techStackRequestDto.getTechNo());
            resultList.add(result);
        }

        return resultList;
    }

    @Override
    public boolean isResumeTechStack(int techNo, int resumeNo) {
        int result = techStackMapper.isResumeTechStack(techNo, resumeNo);

        // 해당 이력서의 기술 스택일 때
        if(result == 1){
            return true;
        }
        return false;
    }

    @Override
    public TechStackResponseDto updateTechStack(TechStackRequestDto techStackRequestDto) {
        techStackMapper.updateTechStack(techStackRequestDto);
        TechStackResponseDto result = techStackMapper.findTechStackByTechNo(techStackRequestDto.getTechNo());
        return result;
    }

    @Override
    public boolean isMyTechStack(int techNo, int memberNo) {
        int result = techStackMapper.isMyTechStack(techNo,memberNo);

        // 내 경험이 맞다면?
        if(result == 1){
            return true;
        } return false;
    }

    @Override
    public boolean isTechStack(int techNo) {
        int result = techStackMapper.isTechStack(techNo);

        if(result == 1){
            return true;
        }
        return false;
    }

    @Override
    public List<TechStackResponseDto> getFullTechStack(int resumeNo) {
        return techStackMapper.getFullTechStack(resumeNo);
    }

    @Override
    public TechStackResponseDto getOneTechStack(int techNo) {
        TechStackResponseDto result = techStackMapper.getOneTechStack(techNo);
        return result;
    }

    @Override
    public List<TechStackResponseDto> deleteTechStack(int techNo, int resumeNo) {
        techStackMapper.deleteTechStack(techNo);
        return techStackMapper.getFullTechStack(resumeNo);
    }
}
