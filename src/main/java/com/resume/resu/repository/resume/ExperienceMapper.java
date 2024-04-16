package com.resume.resu.repository.resume;

import com.resume.resu.vo.request.ExperienceRequestDto;
import com.resume.resu.vo.response.ExperienceResponseDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExperienceMapper {
    @Select("select count(*) from resudb.resume where resumeNo=#{resumeNo} and memberNo=#{memberNo}")
    int isMyResume(@Param("memberNo") int memberNo, @Param("resumeNo") int resumeNo);

    @Insert("insert into experience(resumeNo,exStartDate,exEndDate,exName,exDetail) values(#{resumeNo},#{experienceRequestDto.exStartDate},#{experienceRequestDto.exEndDate},#{experienceRequestDto.exName},#{experienceRequestDto.exDetail})")
    @Options(useGeneratedKeys = true, keyProperty = "experienceRequestDto.exNo")
    int addExperience(@Param("experienceRequestDto") ExperienceRequestDto experienceRequestDto,@Param("resumeNo") int resumeNo);

    @Select("select * from experience where exNo=#{exNo}")
    ExperienceResponseDto findExperienceByExNo(int exNo);

    @Update("update experience set exStartDate=#{experienceRequestDto.exStartDate},exEndDate=#{experienceRequestDto.exEndDate},exName=#{experienceRequestDto.exName},exDetail=#{experienceRequestDto.exDetail} where exNo=#{experienceRequestDto.exNo}")
    int updateExperience(@Param("experienceRequestDto") ExperienceRequestDto experienceRequestDto);

    @Select("select * from experience where resumeNo=#{resumeNo}")
    List<ExperienceResponseDto> getFullExperience(int resumeNo);

    @Select("select count(*) from experience where exNo=#{exNo} and resumeNo=#{resumeNo}")
    int isResumeEx(@Param("exNo") int exNo, @Param("resumeNo") int resumeNo);

    @Select("select count(*) from experience inner join resume using(resumeNo) where memberNo=#{memberNo} and exNo=#{exNo} ")
    int isMyEx (@Param("exNo")int exNo,@Param("memberNo") int memberNo);

    @Select("select count(*) from experience where exNo=#{exNo}")
    int isEx(int exNo);
}
