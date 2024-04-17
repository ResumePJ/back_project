package com.resume.resu.repository.resume;

import com.resume.resu.vo.request.CareerRequestDto;
import com.resume.resu.vo.request.ExperienceRequestDto;
import com.resume.resu.vo.response.CareerResponseDto;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CareerMapper {

    @Insert("insert into career(resumeNo,company,dept,position,carStartDate,carEndDate,carDetail) values(#{resumeNo},#{careerRequestDto.company},#{careerRequestDto.dept},#{careerRequestDto.position},#{careerRequestDto.carStartDate},#{careerRequestDto.carEndDate},#{careerRequestDto.carDetail})")
    @Options(useGeneratedKeys = true, keyProperty = "careerRequestDto.carNo")
    int addCareer(@Param("careerRequestDto") CareerRequestDto careerRequestDto, @Param("resumeNo") int resumeNo);

    @Select("select * from career where carNo=#{carNo}")
    CareerResponseDto findCareerByCarNo (int carNo);

}
