package com.resume.resu.vo.request;

import lombok.Data;

@Data
public class MemberRequestDto {

    /* 휴대폰 번호, 이메일, 사원 번호 중 어떤 것인지 구별하는 것
    * 휴대폰,이메일,회원번호(int형으로 변경 필요)
    * */

    private String identifier;

    private String data;
}
