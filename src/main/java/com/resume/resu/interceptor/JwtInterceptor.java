package com.resume.resu.interceptor;

import com.resume.resu.exception.InvalidTokenException;
import com.resume.resu.exception.MissingTokenException;
import com.resume.resu.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    // JwtUtils 객체 주입
    private final JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 현재 HTTP요청의 URI 반환하는 메서드
        String uri = request.getRequestURI();

        log.info("uri : {}",uri);

        // 요청 헤더에서 access token 가져옴
        String accessToken = jwtUtils.getAcceessToken(request);

        if(accessToken!=null){
            log.info("access Token is not null");

            //토큰 검증
            if(jwtUtils.validateToken(accessToken)){
                log.info("검증 성공한 토큰임. URI : {}",uri);
                return true;
            }else{
                log.info("검증 실패한 토큰임. URI : {}",uri);
//              return false;
                throw new InvalidTokenException("검증 실패한 토큰");
            }
        } else {
          log.info("access Token is null!");
//          return false;
            throw new MissingTokenException("Access token is missing");
        }
    }
}
