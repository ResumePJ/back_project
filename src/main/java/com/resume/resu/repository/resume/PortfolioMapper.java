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
    PortfolioResponseDto findPofolByPofolNo (int pofolNo);
}
