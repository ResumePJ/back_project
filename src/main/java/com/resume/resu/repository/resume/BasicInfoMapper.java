package com.resume.resu.repository.resume;

import com.resume.resu.vo.request.ResumeBasicInfoRequestDTO;
import com.resume.resu.vo.response.MemberDTO;
import com.resume.resu.vo.response.ResumeBasicInfoDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BasicInfoMapper {

    @Select("select count(*) from resudb.resume where resumeNo=#{resumeNo} and memberNo=#{memberNo}")
     int isMyResume(@Param("memberNo") int memberNo, @Param("resumeNo") int resumeNo);

    @Select("select * from resudb.resume where resumeNo=#{resumeNo}")
    ResumeBasicInfoDTO getResumeBasicInfo(@Param("resumeNo") int resumeNo);

    @Select("select * from resudb.member where memberNo=#{memberNo}")
    MemberDTO getMemberInfo(@Param("memberNo") int memberNo);

    @Insert("insert into resudb.resume(memberNo, type, name, gender, birth, phone, address, intro) values(#{memberInfo.memberNo},#{resumeBasicInfoRequestDTO.type},#{memberInfo.name},#{resumeBasicInfoRequestDTO.gender},#{memberInfo.birth},#{resumeBasicInfoRequestDTO.phone},#{resumeBasicInfoRequestDTO.address},#{resumeBasicInfoRequestDTO.intro})")
    int insertResumeBasicInfo(@Param("resumeBasicInfoRequestDTO") ResumeBasicInfoRequestDTO resumeBasicInfoRequestDTO,@Param("memberInfo") MemberDTO memberInfo);

    @Select("select resumeNo from resudb.resume where memberNo=#{memberNo} order by resumeNo desc limit 1")
    int findLastResumeNoById(@Param("memberNo")int memberNo);
}
