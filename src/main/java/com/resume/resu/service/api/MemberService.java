package com.resume.resu.service.api;

import com.resume.resu.vo.response.MemberDTO;

public interface MemberService {

    public MemberDTO getResumeMemberInfo(int memberNo);
}
