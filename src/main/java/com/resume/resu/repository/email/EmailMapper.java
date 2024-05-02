package com.resume.resu.repository.email;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmailMapper {

    @Select("select count(*) from member where name=#{name} and birth=#{birth} and email=#{email} ")
    int isMember (@Param("name")String name, @Param("birth")String birth, @Param("email")String email);
}
