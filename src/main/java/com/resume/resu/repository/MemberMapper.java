package com.resume.resu.repository;

import com.resume.resu.vo.response.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberMapper {

    @Select("select * from resudb.member where memberNo=#{memberNo}")
     MemberDTO getResumeMemberInfo(int memberNo);
}
