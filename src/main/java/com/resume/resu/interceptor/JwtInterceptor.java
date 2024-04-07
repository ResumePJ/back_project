package com.resume.resu.interceptor;

import com.resume.resu.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    // JwtUtils 객체 주입
    private JwtUtils jwtUtils;

    // 스프링이 클래스를 인스턴스화 할 때, 생성자를 통해 의존성 주입
    @Autowired
    public JwtInterceptor(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 현재 HTTP요청의 URI 반환하는 메서드
        String uri = request.getRequestURI();

        // 요청 헤더에서 access token 가져옴
        String accessToken = jwtUtils.getAcceessToken(request);

        if(accessToken!=null){
            logger.info("access Token is not null");

            //토큰 검증
            if(jwtUtils.validateToken(accessToken)){
                logger.info("검증 성공한 토큰임. URI : {}",uri);
                return true;
            }else{
                logger.info("검증 실패한 토큰임. URI : {}",uri);
                return false;
            }
        } else {
          logger.info("access Token is null!");
          return false;
        }
    }
}
