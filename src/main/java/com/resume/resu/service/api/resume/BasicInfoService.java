package com.resume.resu.service.api.resume;

import com.resume.resu.vo.request.MultipartUploadRequestDto;
import com.resume.resu.vo.request.ResumeBasicInfoRequestDTO;
import com.resume.resu.vo.response.MemberDTO;
import com.resume.resu.vo.response.MultipartUploadResponseDto;
import com.resume.resu.vo.response.ResumeBasicInfoDTO;
import org.springframework.core.io.Resource;

public interface BasicInfoService {

    boolean isMyResume(int memberNo, int resumeNo);

    ResumeBasicInfoDTO getResumeBasicInfo(int resumeNo);

    MemberDTO getMemberInfo(int memberNo);

    int insertResumeBasicInfo(ResumeBasicInfoRequestDTO resumeBasicInfoRequestDTO, MemberDTO memberInfo);

    int findLastResumeNoById(int memberNo);

    MultipartUploadResponseDto uploadFile(MultipartUploadRequestDto dto);

    Resource download(int resumeNo);

    ResumeBasicInfoDTO updateResumeBasicInfo(int resumeNo,ResumeBasicInfoRequestDTO resumeBasicInfoRequestDTO);

    boolean isResume(int resumeNo);
}
