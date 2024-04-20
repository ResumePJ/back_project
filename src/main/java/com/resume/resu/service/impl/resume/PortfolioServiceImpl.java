package com.resume.resu.service.impl.resume;

import com.resume.resu.repository.resume.PortfolioMapper;
import com.resume.resu.service.api.resume.PortfolioService;
import com.resume.resu.vo.request.CareerRequestDto;
import com.resume.resu.vo.request.PortfolioRequestDto;
import com.resume.resu.vo.response.CareerResponseDto;
import com.resume.resu.vo.response.PortfolioResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PortfolioServiceImpl implements PortfolioService {

    public final PortfolioMapper portfolioMapper;

    @Override
    public List<PortfolioResponseDto> addPortfolio(List<PortfolioRequestDto> getListDto, int resumeNo) {
        List<PortfolioResponseDto> resultList = new ArrayList<>();

        for (PortfolioRequestDto portfolioRequestDto : getListDto) {
            portfolioMapper.addPortfolio(portfolioRequestDto,resumeNo);

            log.info("PortfolioRequestDto의 pofolNo : {}",portfolioRequestDto.getPofolNo());
            PortfolioResponseDto result = portfolioMapper.findPofolByPofolNo(portfolioRequestDto.getPofolNo());
            resultList.add(result);
        }

        return resultList;
    }
}