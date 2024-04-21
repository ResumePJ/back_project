package com.resume.resu.service.api.member;

import com.resume.resu.vo.request.MemberRequestDto;
import com.resume.resu.vo.request.UpdateMemberRequestDto;
import com.resume.resu.vo.response.MemberDTO;

public interface MemberService {
    MemberDTO getMemberInfo(MemberRequestDto memberRequestDto);

    MemberDTO getMemberInfoAfterLogin(int memberNo);

    MemberDTO updateMemberInfo(UpdateMemberRequestDto updateMemberRequestDto,int memberNo);
}
