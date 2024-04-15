package com.resume.resu.service.impl.resume;

import com.resume.resu.repository.resume.ExperienceMapper;
import com.resume.resu.service.api.resume.ExperienceService;
import com.resume.resu.vo.request.ExperienceRequestDto;
import com.resume.resu.vo.response.ExperienceResponseDto;
import com.resume.resu.vo.response.MultipartUploadResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExperienceServiceImpl implements ExperienceService {

    public final ExperienceMapper experienceMapper;

    @Override
    public boolean isMyResume(int memberNo, int resumeNo) {
        int count = experienceMapper.isMyResume(memberNo,resumeNo);
        if(count==0){
            return false;
        }
        return true;
    }

    @Override
    public List<ExperienceResponseDto> addExperience(List<ExperienceRequestDto> getListDto, int resumeNo) {
        List<ExperienceResponseDto> resultList = new ArrayList<>();

        for (ExperienceRequestDto experienceRequestDto : getListDto) {
            int insertRow = experienceMapper.addExperience(experienceRequestDto,resumeNo);
            log.info("addExperience의 insertRow : {}",insertRow);

            if (insertRow == 1) {
                log.info("입력한 경험의 exNo : {}", experienceRequestDto.getExNo());
                ExperienceResponseDto result = experienceMapper.findExperienceByExNo(experienceRequestDto.getExNo());
                resultList.add(result);
            }
        }

        if (getListDto.size() == resultList.size()) {
            return resultList;
        } else {
            return null;
        }
    }

    }
