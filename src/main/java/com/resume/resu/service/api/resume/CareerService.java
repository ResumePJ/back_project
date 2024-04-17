package com.resume.resu.service.api.resume;


import com.resume.resu.vo.request.CareerRequestDto;
import com.resume.resu.vo.request.ExperienceRequestDto;
import com.resume.resu.vo.response.CareerResponseDto;
import com.resume.resu.vo.response.ExperienceResponseDto;

import java.util.List;

public interface CareerService {
    List<CareerResponseDto> addCareer (List<CareerRequestDto> getListDto, int resumeNo);

    boolean isResumeCareer(int carNo, int resumeNo);

    CareerResponseDto updateCareer(CareerRequestDto careerRequestDto);

    boolean isMyCareer(int carNo,int memberNo);

    boolean isCareer(int carNo);
}
