package com.resume.resu.repository.resume;

import com.resume.resu.vo.request.ExperienceRequestDto;
import com.resume.resu.vo.response.ExperienceResponseDto;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ExperienceMapper {
    @Select("select count(*) from resudb.resume where resumeNo=#{resumeNo} and memberNo=#{memberNo}")
    int isMyResume(@Param("memberNo") int memberNo, @Param("resumeNo") int resumeNo);

    @Insert("insert into experience(resumeNo,exStartDate,exEndDate,exName,exDetail) values(#{resumeNo},#{experienceRequestDto.exStartDate},#{experienceRequestDto.exEndDate},#{experienceRequestDto.exName},#{experienceRequestDto.exDetail})")
    @Options(useGeneratedKeys = true, keyProperty = "experienceRequestDto.exNo")
    int addExperience(@Param("experienceRequestDto") ExperienceRequestDto experienceRequestDto,@Param("resumeNo") int resumeNo);

    @Select("select * from experience where exNo=#{exNo}")
    ExperienceResponseDto findExperienceByExNo(int exNo);
}
