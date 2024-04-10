package com.resume.resu.service.impl.resume;

import com.resume.resu.repository.resume.BasicInfoMapper;
import com.resume.resu.service.api.resume.BasicInfoService;
import com.resume.resu.vo.request.ResumeBasicInfoRequestDTO;
import com.resume.resu.vo.response.MemberDTO;
import com.resume.resu.vo.response.ResumeBasicInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicInfoServiceImpl implements BasicInfoService {

    //TODO :  사원의 정보 받아오는 것 만들 것

    public final BasicInfoMapper basicInfoMapper;

    @Override
    public boolean isMyResume(int memberNo, int resumeNo) {
         int count = basicInfoMapper.isMyResume(memberNo,resumeNo);
         if(count==0){
             return false;
         }
         return true;
    }

    @Override
    public ResumeBasicInfoDTO getResumeBasicInfo(int resumeNo) {
        return basicInfoMapper.getResumeBasicInfo(resumeNo);
    }

    @Override
    public MemberDTO getMemberInfo(int memberNo) {
        return basicInfoMapper.getMemberInfo(memberNo);
    }

    @Override
    public int insertResumeBasicInfo(ResumeBasicInfoRequestDTO resumeBasicInfoRequestDTO, MemberDTO memberInfo) {
        return basicInfoMapper.insertResumeBasicInfo(resumeBasicInfoRequestDTO,memberInfo);
    }

    @Override
    public int findLastResumeNoById(int memberNo) {
        return basicInfoMapper.findLastResumeNoById(memberNo);
    }


}
