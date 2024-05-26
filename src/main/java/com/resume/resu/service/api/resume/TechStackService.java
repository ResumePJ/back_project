package com.resume.resu.service.api.resume;

import com.resume.resu.vo.request.TechStackRequestDto;
import com.resume.resu.vo.request.TechStackRequestDtoList;
import com.resume.resu.vo.response.TechStackResponseDto;

import java.util.List;

public interface TechStackService {

    List<TechStackResponseDto> addTechStack (TechStackRequestDtoList getListDto, int resumeNo);

    boolean isResumeTechStack(int techNo, int resumeNo);

    TechStackResponseDto updateTechStack(TechStackRequestDto techStackRequestDto);

    boolean isMyTechStack(int techNo, int memberNo);

    boolean isTechStack(int techNo);

    List<TechStackResponseDto> getFullTechStack(int resumeNo);

    TechStackResponseDto getOneTechStack(int techNo);

    List<TechStackResponseDto> deleteTechStack(int techNo, int resumeNo);
}
