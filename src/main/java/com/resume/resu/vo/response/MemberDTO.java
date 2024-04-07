package com.resume.resu.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
/*TODO : 파일 합친 후, 회원가입 쪽으로 */
public class MemberDTO {
    private int memberNo; // 회원 번호
    private String email; // 이메일
    private String password; // 비밀번호
    private String name; // 이름(닉네임)
    private String phone; // 전화번호
    private Date birth; // 생년월일
    private Date joinDate; // 가입 일자
    private int type; // 가입 유형
}
