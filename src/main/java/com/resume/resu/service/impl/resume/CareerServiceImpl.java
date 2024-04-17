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

    @Override
    public boolean isResumeCareer(int carNo, int resumeNo) {
        int result= careerMapper.isResumeCareer(carNo,resumeNo);

        // 해당 이력서의 경력일 때
        if(result ==1){
            return true;
        }
        return false;
    }

    @Override
    public CareerResponseDto updateCareer(CareerRequestDto careerRequestDto) {
        careerMapper.updateCareer(careerRequestDto);
        CareerResponseDto result = careerMapper.findCareerByCarNo(careerRequestDto.getCarNo());
        return result;
    }

    @Override
    public boolean isMyCareer(int carNo, int memberNo) {
        int result = careerMapper.isMyCareer(carNo,memberNo);

        // 내 경험이 맞다면?
        if(result ==1){
            return true;
        } return false;
    }

    @Override
    public boolean isCareer(int carNo) {
        int result = careerMapper.isCareer(carNo);

        if(result ==1){
            return true;
        }return false;
    }

    @Override
    public List<CareerResponseDto> getFullCareer(int resumeNo) {
        return careerMapper.getFullCareer(resumeNo);
    }

    @Override
    public CareerResponseDto getOneCareer(int carNo) {
        CareerResponseDto result = careerMapper.getOneCareer(carNo);
        return result;
    }
}
