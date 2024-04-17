package com.resume.resu.repository.resume;

import com.resume.resu.vo.request.CareerRequestDto;
import com.resume.resu.vo.request.ExperienceRequestDto;
import com.resume.resu.vo.response.CareerResponseDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CareerMapper {

    @Insert("insert into career(resumeNo,company,dept,position,carStartDate,carEndDate,carDetail) values(#{resumeNo},#{careerRequestDto.company},#{careerRequestDto.dept},#{careerRequestDto.position},#{careerRequestDto.carStartDate},#{careerRequestDto.carEndDate},#{careerRequestDto.carDetail})")
    @Options(useGeneratedKeys = true, keyProperty = "careerRequestDto.carNo")
    int addCareer(@Param("careerRequestDto") CareerRequestDto careerRequestDto, @Param("resumeNo") int resumeNo);

    @Select("select * from career where carNo=#{carNo}")
    CareerResponseDto findCareerByCarNo (int carNo);

    @Select("select count(*) from career where carNo=#{carNo} and resumeNo=#{resumeNo}")
    int isResumeCareer(@Param("carNo") int carNo, @Param("resumeNo") int resumeNo);

    @Update("update career set company=#{careerRequestDto.company}, dept=#{careerRequestDto.dept}, position=#{careerRequestDto.position}, carStartDate=#{careerRequestDto.carStartDate}, carEndDate=#{careerRequestDto.carEndDate}, carDetail=#{careerRequestDto.carDetail} where carNo=#{careerRequestDto.carNo}")
    int updateCareer(@Param("careerRequestDto") CareerRequestDto careerRequestDto);

    @Select("select count(*) from career inner join resume using(resumeNo) where carNo=#{carNo} and memberNo=#{memberNo}")
    int isMyCareer(@Param("carNo") int carNo,@Param("memberNo") int memberNo);

    @Select("select count(*) from career where carNo=#{carNo}")
    int isCareer(int carNo);

    @Select("select * from career where resumeNo=#{resumeNo}")
    List<CareerResponseDto> getFullCareer(int resumeNo);

    @Select("select * from career where carNo=#{carNo}")
    CareerResponseDto getOneCareer(int carNo);

    @Delete("delete from career where carNo=#{carNo} ")
    int deleteCareer(int carNo);

}
