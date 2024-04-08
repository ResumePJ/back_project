package com.resume.resu.service.api.resume;

import com.resume.resu.vo.response.MemberDTO;
import com.resume.resu.vo.response.ResumeBasicInfoDTO;

public interface BasicInfoService {

    boolean isMyResume(int memberNo, int resumeNo);

    ResumeBasicInfoDTO getResumeBasicInfo(int resumeNo);
}
