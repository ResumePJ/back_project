package com.resume.resu.util;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;


@Component
@Slf4j
public class JwtUtils {

    // JwtUtils 클래스에 대한 로거 생성
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // JJWT 라이브러리의 API 사용해 HS256(HMAC SHA-256) 알고리즘을 사용하는 비밀 키 생성
    private static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    //access token 유지 시간 선언 (밀리초) - 6시간
//    public static final long ACCESS_TOKEN_VALIDATION_SECOND = 1;
    public static final long ACCESS_TOKEN_VALIDATION_SECOND = 1000 * 60 * 60 * 6;
    // access 토큰 생성
    public String createAccessToken(String email, String name) {

        // 토큰 만료 시간 선언 해둔 것 가져와 설정
        // 만료 시간 = 현재 시간 + 선언 해둔 밀리 초
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_VALIDATION_SECOND);

        // Jwts 클래스 : JWT 인스턴스를 생성하는 팩토리 클래스

        return Jwts.builder() // 해당 팩토리 클래스가 빌더 패턴을 구현. 빌더 객체 가져옴
                .setSubject(email) // 토큰에 대한 소유자 식별 역할
                .claim("name", name) // 토큰에 담길 사용자의 추가 정보 (JSON 형식 등 사용)
                .setIssuedAt(now) // 토큰이 발급된 시간
                .setExpiration(expiration) // 토큰의 만료 시간을 설정
                .signWith(secretKey) //서명
                .compact(); // 설정된 정보를 바탕으로 JWT 생성, 문자열 형태로 반환
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            /*
               Jwts.parserBuilder() : JWT 파서를 빌드하기 위한 빌더 반환
               setSigningKey(secretKey) : 빌더에 서명 키 설정
               build() : 해당 설정을 사용해 JWT 파서를 빌드 -> 최종적으로 파서 인스턴스 반환
               parseClaimJws(token) : 주어진 토큰 파싱, 서명 검증
                - JWT의 서명이 유효 => JWT의 클레임 반환
                - JWT의 서명이 유효하지 않으면 => SignatureException 발생
            */
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token); //Jws 반환
            return true; // 예외가 발생하지 않음. 토큰 파싱 중 문제가 발생하지 않고 마무리 됨
        } catch (SignatureException e) {
            log.info("서명이 잘못 됨");
        } catch (ExpiredJwtException e) {
            log.info("토큰 만료");
            /* TODO : 토큰 만료시 처리 코드 추가할 것*/

        } catch (IllegalArgumentException | MalformedJwtException e) {
            log.info("올바르지 못한 토큰");
        }

        //catch 문 실행 시, return false 반드시 실행됨
        return false;
    }

    // 토큰에서 email 추출해 반환
    // setSubject로 email을 jwt에 설정했기 때문에
    public String getId(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰에서 name 추출해 반환
    public String getName(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("name").toString();
    }

    //HttpServletRequest에서 Authorization Header를 통해 access token 추출 - (request header에서 Authorization header를 읽어와 access token 추출)
    public String getAcceessToken(HttpServletRequest httpServletRequest) {

        // Authorization 헤더 가져옴
        String bearerToken = httpServletRequest.getHeader("Authorization");

        // Authorization 헤더가 null 혹은 공백이 아닌지 확인 , Bearer 로 시작하는지 확인
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {

            //Bearer (6번)다음에 오는 문자(7번)부터 리턴
            return bearerToken.substring(7);
        }
        return null;
    }

    // TODO: [로그인 실패] 시 어떻게 할지? 아래 메서드 삭제 해야 할 수도 있음
    public String determineRedirectURI(HttpServletRequest httpServletRequest, String URI) {
        String token = getAcceessToken(httpServletRequest);
        if (token != null) {
            return URI;
        }
        return null;
    }

}
