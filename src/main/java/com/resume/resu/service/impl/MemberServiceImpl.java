package com.resume.resu.service.impl;

import com.resume.resu.repository.MemberMapper;
import com.resume.resu.service.api.MemberService;
import com.resume.resu.vo.response.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    public final MemberMapper memberMapper;

    @Override
    public MemberDTO getResumeMemberInfo(int memberNo) {
        MemberDTO member = memberMapper.getResumeMemberInfo(memberNo);
        return member;
    }
}
