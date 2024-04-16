package com.resume.resu.service.api.resume;

import com.resume.resu.vo.request.ExperienceRequestDto;
import com.resume.resu.vo.response.ExperienceResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ExperienceService {

    boolean isMyResume(int memberNo, int resumeNo);

    List<ExperienceResponseDto> addExperience(List<ExperienceRequestDto> getListDto, int resumeNo);

    ExperienceResponseDto updateExperience (ExperienceRequestDto experienceRequestDto);
}
