package com.resume.resu.repository.login;

import com.resume.resu.vo.request.EmailLoginRequestDto;
import com.resume.resu.vo.response.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface LoginMapper {

    @Select("SELECT * FROM resudb.MEMBER WHERE EMAIL = #{emailLoginRequestDto.email} AND password=#{emailLoginRequestDto.password}")
    MemberDTO findMemberInfoByEmail(@Param("emailLoginRequestDto") EmailLoginRequestDto emailLoginRequestDto);

}
