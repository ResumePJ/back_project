package com.resume.resu.repository.resume;

import com.resume.resu.vo.response.MemberDTO;
import com.resume.resu.vo.response.ResumeBasicInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BasicInfoMapper {

    @Select("select count(*) from resudb.resume where resumeNo=#{resumeNo} and memberNo=#{memberNo}")
     int isMyResume(@Param("memberNo") int memberNo, @Param("resumeNo") int resumeNo);

    @Select("select * from resudb.resume where resumeNo=#{resumeNo}")
    ResumeBasicInfoDTO getResumeBasicInfo(@Param("resumeNo") int resumeNo);
}
