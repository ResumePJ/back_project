package com.resume.resu.repository.member;

import com.resume.resu.vo.request.MemberRequestDto;
import com.resume.resu.vo.request.UpdateMemberRequestDto;
import com.resume.resu.vo.response.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemberMapper {

    @Select("select * from member where phone=#{memberRequestDto.data}")
    MemberDTO findMemberInfoByPhone(@Param("memberRequestDto") MemberRequestDto memberRequestDto);

    @Select("select * from member where email=#{memberRequestDto.data}")
    MemberDTO findMemberInfoByEmail(@Param("memberRequestDto") MemberRequestDto memberRequestDto);

    @Select("select * from member where memberNo=#{memberNo}")
    MemberDTO findMemberInfoByMemberNo(int memberNo);

    @Update("update member set email=#{updateMemberRequestDto.email},password=#{updateMemberRequestDto.password},name=#{updateMemberRequestDto.name},phone=#{updateMemberRequestDto.phone},birth=#{updateMemberRequestDto.birth},joinDate=#{updateMemberRequestDto.joinDate},type=#{updateMemberRequestDto.type} where memberNo=#{memberNo}")
    int updateMemberInfo(@Param("updateMemberRequestDto") UpdateMemberRequestDto updateMemberRequestDto,@Param("memberNo") int memberNo);

}
