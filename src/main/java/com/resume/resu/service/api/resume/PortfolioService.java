package com.resume.resu.service.api.resume;

import com.resume.resu.vo.request.PortfolioRequestDto;
import com.resume.resu.vo.response.PortfolioResponseDto;

import java.util.List;

public interface PortfolioService {

    List<PortfolioResponseDto> addPortfolio(List<PortfolioRequestDto> getListDto, int resumeNo);

    boolean isResumePofol(int pofolNo, int resumeNo);

    PortfolioResponseDto updatePortfolio(PortfolioRequestDto portfolioRequestDto);

    boolean isMyPofol(int pofolNo, int memberNo);

    boolean isPofol(int pofolNo);

    List<PortfolioResponseDto> getFullPortfolio(int resumeNo);
}
