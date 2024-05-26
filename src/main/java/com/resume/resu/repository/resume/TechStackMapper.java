package com.resume.resu.repository.resume;

import com.resume.resu.vo.request.TechStackRequestDto;
import com.resume.resu.vo.response.TechStackResponseDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TechStackMapper {

    // 기술 스택 추가
    @Insert("INSERT INTO techStack (resumeNo, type, techName, techLevel) " +
            "VALUES (#{resumeNo}, #{techStackRequestDto.type}, #{techStackRequestDto.techName}, #{techStackRequestDto.techLevel})")
    @Options(useGeneratedKeys = true, keyProperty = "techStackRequestDto.techNo")
    int addTechStack(@Param("techStackRequestDto") TechStackRequestDto techStackRequestDto, @Param("resumeNo") int resumeNo);

    // 특정 기술 스택 조회 (상세 포함)
    @Select("SELECT * FROM techStack WHERE techNo = #{techNo}")
    TechStackResponseDto findTechStackByTechNo(int techNo);

    // 특정 이력서에 속한 기술 스택인지 확인
    @Select("SELECT COUNT(*) FROM techStack WHERE techNo = #{techNo} AND resumeNo = #{resumeNo}")
    int isResumeTechStack(@Param("techNo") int techNo, @Param("resumeNo") int resumeNo);

    // 기술 스택 업데이트
    @Update("UPDATE techStack SET type = #{techStackRequestDto.type}, techName = #{techStackRequestDto.techName}, techLevel = #{techStackRequestDto.techLevel} " +
            "WHERE techNo = #{techStackRequestDto.techNo}")
    int updateTechStack(@Param("techStackRequestDto") TechStackRequestDto techStackRequestDto);

    // 특정 회원의 기술 스택인지 확인
    @Select("SELECT COUNT(*) FROM techStack INNER JOIN resume USING(resumeNo) WHERE techNo = #{techNo} AND memberNo = #{memberNo}")
    int isMyTechStack(@Param("techNo") int techNo, @Param("memberNo") int memberNo);

    // 특정 기술 스택이 존재하는지 확인
    @Select("SELECT COUNT(*) FROM techStack WHERE techNo = #{techNo}")
    int isTechStack(int techNo);

    // 특정 이력서에 속한 모든 기술 스택 조회
    @Select("SELECT * FROM techStack WHERE resumeNo = #{resumeNo}")
    List<TechStackResponseDto> getFullTechStack(int resumeNo);

    @Select("SELECT * FROM techStack WHERE techNo=#{techNo}")
    TechStackResponseDto getOneTechStack(int techNo);

    // 기술 스택 삭제
    @Delete("DELETE FROM techStack WHERE techNo = #{techNo}")
    int deleteTechStack(int techNo);
}
