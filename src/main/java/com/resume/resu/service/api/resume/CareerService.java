package com.resume.resu.service.api.resume;


import com.resume.resu.vo.request.CareerRequestDto;
import com.resume.resu.vo.request.CareerRequestDtoList;
import com.resume.resu.vo.request.ExperienceRequestDto;
import com.resume.resu.vo.response.CareerResponseDto;
import com.resume.resu.vo.response.ExperienceResponseDto;

import java.util.List;

public interface CareerService {
    List<CareerResponseDto> addCareer (CareerRequestDtoList getListDto, int resumeNo);

    boolean isResumeCareer(int carNo, int resumeNo);

    CareerResponseDto updateCareer(CareerRequestDto careerRequestDto);

    boolean isMyCareer(int carNo,int memberNo);

    boolean isCareer(int carNo);

    List<CareerResponseDto> getFullCareer(int resumeNo);

    CareerResponseDto getOneCareer(int carNo);

    List<CareerResponseDto> deleteCareer (int carNo,int resumeNo);
}
