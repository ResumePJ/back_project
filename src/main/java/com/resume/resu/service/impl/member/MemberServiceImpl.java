package com.resume.resu.service.impl.member;

import com.resume.resu.repository.member.MemberMapper;
import com.resume.resu.service.api.member.MemberService;
import com.resume.resu.vo.request.MemberRequestDto;
import com.resume.resu.vo.request.UpdateMemberRequestDto;
import com.resume.resu.vo.response.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

    public final MemberMapper memberMapper;

    @Override
    public MemberDTO getMemberInfo(MemberRequestDto memberRequestDto) {

        MemberDTO member = null;

        // 휴대폰 번호로 회원 정보 조회
        if(memberRequestDto.getIdentifier().equals("휴대폰")){
            member = memberMapper.findMemberInfoByPhone(memberRequestDto);
        }

        // 이메일로 회원 정보 조회
        else if(memberRequestDto.getIdentifier().equals("이메일")){
            member = memberMapper.findMemberInfoByEmail(memberRequestDto);
        }

        // 회원 번호로 회원 정보 조회
        else if(memberRequestDto.getIdentifier().equals("회원번호")){
            int memberNo = Integer.parseInt(memberRequestDto.getData()); // string형 => int형
            member = memberMapper.findMemberInfoByMemberNo(memberNo);
        }

        return member;
    }

    @Override
    public MemberDTO getMemberInfoAfterLogin(int memberNo) {
        MemberDTO member = null;
        member = memberMapper.findMemberInfoByMemberNo(memberNo);
        return member;
    }

    @Override
    public MemberDTO updateMemberInfo(UpdateMemberRequestDto updateMemberRequestDto,int memberNo) {
        int result = memberMapper.updateMemberInfo(updateMemberRequestDto,memberNo);

        if(result < 1){
            return null;
        }else{
            return memberMapper.findMemberInfoByMemberNo(memberNo);
        }
    }
}
