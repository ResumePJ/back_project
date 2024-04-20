package com.resume.resu.repository.resume;

import com.resume.resu.vo.request.PortfolioRequestDto;
import com.resume.resu.vo.response.PortfolioResponseDto;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PortfolioMapper {

    @Insert("insert into portfolio(resumeNo, title, link, pofolDetail) values (#{resumeNo},#{portfolioRequestDto.title},#{portfolioRequestDto.link},#{portfolioRequestDto.pofolDetail})")
    @Options(useGeneratedKeys = true, keyProperty = "portfolioRequestDto.pofolNo")
    int addPortfolio(@Param("portfolioRequestDto") PortfolioRequestDto portfolioRequestDto, @Param("resumeNo") int resumeNo);

    @Select("select * from portfolio where pofolNo=#{pofolNo}")
    PortfolioResponseDto findPofolByPofolNo (@Param("pofolNo") int pofolNo);

    @Select("select count(*) from portfolio where pofolNo=#{pofolNo} and resumeNo=#{resumeNo}")
    int isResumePofol(@Param("pofolNo") int pofolNo,@Param("resumeNo") int resumeNo);

    @Update("update portfolio set title=#{portfolioRequestDto.title},link=#{portfolioRequestDto.link}, pofolDetail=#{portfolioRequestDto.pofolDetail} where pofolNo=#{portfolioRequestDto.pofolNo}")
    int updatePortfolio(@Param("portfolioRequestDto") PortfolioRequestDto portfolioRequestDto);

    @Select("select count(*) from portfolio inner join resume using(resumeNo) where pofolNo=#{pofolNo} and memberNo=#{memberNo}")
    int isMyPofol(@Param("pofolNo") int pofolNo, @Param("memberNo") int memberNo);

    @Select("select count(*) from portfolio where pofolNo=#{pofolNo}")
    int isPofol(@Param("pofolNo") int pofolNo);
}
