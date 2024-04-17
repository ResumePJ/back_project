package com.resume.resu.service.impl.resume;

import com.resume.resu.repository.resume.CareerMapper;
import com.resume.resu.service.api.resume.CareerService;
import com.resume.resu.vo.request.CareerRequestDto;
import com.resume.resu.vo.request.ExperienceRequestDto;
import com.resume.resu.vo.response.CareerResponseDto;
import com.resume.resu.vo.response.ExperienceResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CareerServiceImpl implements CareerService {

    public final CareerMapper careerMapper;
    @Override
    public List<CareerResponseDto> addCareer(List<CareerRequestDto> getListDto, int resumeNo) {
        List<CareerResponseDto> resultList = new ArrayList<>();

        for (CareerRequestDto careerRequestDto : getListDto) {
            careerMapper.addCareer(careerRequestDto,resumeNo);

            log.info("careerRequestDto의 carNo : {}",careerRequestDto.getCarNo());
            CareerResponseDto result = careerMapper.findCareerByCarNo(careerRequestDto.getCarNo());
            resultList.add(result);
        }

        return resultList;

    }
}
